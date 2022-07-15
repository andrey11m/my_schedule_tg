package tg.com.my_schedule_tg.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.com.my_schedule_tg.service.TelegramBotService;
import tg.com.my_schedule_tg.service.EventService;

import static tg.com.my_schedule_tg.util.Constants.STATUS_CALLBACK_DATE;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    private final TelegramBot bot;
    private final EventService eventService;

    public void serve() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();
        if (message != null) {
            eventService.handler(message, bot);
        }
        if (callbackQuery != null && !callbackQuery.data().equals(STATUS_CALLBACK_DATE)) {
            eventService.remove(callbackQuery, bot);
        }

    }
}
