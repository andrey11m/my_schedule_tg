package tg.com.my_schedule_tg.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.com.my_schedule_tg.model.Event;
import tg.com.my_schedule_tg.repo.EventRepo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tg.com.my_schedule_tg.util.Constants.*;
import static tg.com.my_schedule_tg.util.DateTimeFormatterConstants.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepo eventRepo;
    private final Map<Long, Event> changeMap = new HashMap<>();

    public void handler(Message message, TelegramBot bot) {
        String text = message.text();
        Long chatId = message.chat().id();
        if (message.text().equals(NEW_EVENT) || changeMap.containsKey(chatId)) {
            addEvent(message, bot, text, chatId);
        }
        if (text.equals(ALL_EVENTS)) {
            getAllEvents(message, bot, chatId);
        }
        if (text.equals(WEEK_EVENTS)) {
            getEventsForWeek(message, bot, chatId);
        }
    }

    private void getEventsForWeek(Message message, TelegramBot bot, Long userId) {
        List<Event> events = eventRepo.findEventsByUserId(userId);
        List<Event> eventsForWeek = events.stream()
                .filter(event -> event.getDateTime().isBefore(LocalDateTime.now().plusWeeks(1)))
                .collect(Collectors.toList());
        if (eventsForWeek.isEmpty()) {
            bot.execute(new SendMessage(message.chat().id(), NO_EVENTS));
        } else {
            eventsForWeek.sort(Comparator.comparing(Event::getDateTime));
            eventsForWeek.forEach(event -> bot.execute(getSendMessage(event)));
        }
    }

    private void getAllEvents(Message message, TelegramBot bot, Long userID) {
        List<Event> events = eventRepo.findEventsByUserId(userID);
        if (events.isEmpty()) {
            bot.execute(new SendMessage(message.chat().id(), NO_EVENTS));
        } else {
            events.sort(Comparator.comparing(Event::getDateTime));
            events.forEach(event -> bot.execute(getSendMessage(event)));
        }
    }

    public SendMessage getSendMessage(Event event) {
        final String TEXT_BUTTON = "remove";
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(TEXT_BUTTON)
                        .callbackData(event.getId().toString()));
        StringBuilder answer = new StringBuilder();
        answer.append(event.getName())
                .append("\n")
                .append(event.getDateTime().format(DAY_OF_WEEK_FORMATTER))
                .append("\n").append(event.getDateTime().format(DATE_FORMATTER));
        return new SendMessage(event.getUserId(), answer.toString()).replyMarkup(markup);
    }

    private void addEvent(Message message, TelegramBot bot, String text, Long userId) {
        final String YES_TEXT_BUTTON = "Yes";
        final String NO_TEXT_BUTTON = "No";
        if (changeMap.containsKey(userId)) {
            Event event = changeMap.get(userId);
            switch (changeMap.get(userId).getState()) {
                case NAME_STATUS:
                    event.setName(text);
                    event.setState(DATE_STATUS);
                    changeMap.put(userId, event);
                    bot.execute(new SendMessage(message.chat().id(), NEW_EVENT_ENTER_DATE_MESSAGE));
                    break;
                case DATE_STATUS:
                    text = text + " 2022";
                    if (checkDateFormat(text)) {
                        event.setDateTime(LocalDateTime.parse(text, FULL_FORMATTER));
                        event.setState(REPEATABLE_STATUS);
                        changeMap.put(userId, event);
                        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(
                                new KeyboardButton(YES_TEXT_BUTTON), new KeyboardButton(NO_TEXT_BUTTON))
                                .selective(true)
                                .resizeKeyboard(true);
                        bot.execute(new SendMessage(userId, NEW_EVENT_ENTER_REPEAT_MESSAGE).replyMarkup(markup));
                    } else {
                        bot.execute(new SendMessage(userId, NEW_EVENT_ENTER_DATE_MESSAGE));
                    }
                    break;
                case REPEATABLE_STATUS:
                    event.setState(COMPLETED_STATUS);
                    if (text.equals(YES_TEXT_BUTTON)) {
                        for (int i = 0; i < 4; i++) {
                            eventRepo.save(Event
                                    .builder()
                                    .name(event.getName())
                                    .dateTime(event.getDateTime().plusWeeks(i))
                                    .userId(event.getUserId())
                                    .state(event.getState())
                                    .build());
                        }
                    } else {
                        eventRepo.save(event);
                    }
                    bot.execute(new SendMessage(message.chat().id(), NEW_EVENT_CREATED_MESSAGE));
                    changeMap.remove(userId);
                    break;
            }
        } else {
            bot.execute(new SendMessage(message.chat().id(), NEW_EVENT_ENTER_NAME_MESSAGE));
            Event event = Event.builder().state(NAME_STATUS).userId(userId).build();
            changeMap.put(userId, event);
        }
    }

    private boolean checkDateFormat(String text) {
        try {
            FULL_FORMATTER.parse(text);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void remove(CallbackQuery callbackQuery, TelegramBot bot) {
        final String TEXT_BUTTON = "removed \uD83D\uDC80";
        Long eventId = Long.valueOf(callbackQuery.data());
        eventRepo.findById(eventId).ifPresent(eventRepo::delete);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(TEXT_BUTTON).callbackData(STATUS_CALLBACK_DATE));
        bot.execute(new EditMessageReplyMarkup(callbackQuery.from().id(), callbackQuery.message().messageId()).replyMarkup(markup));
    }
}
