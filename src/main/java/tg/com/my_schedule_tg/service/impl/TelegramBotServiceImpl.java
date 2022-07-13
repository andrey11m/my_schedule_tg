package tg.com.my_schedule_tg.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.com.my_schedule_tg.repo.TgUserRepo;
import tg.com.my_schedule_tg.service.TelegramBotService;
import tg.com.my_schedule_tg.service.TgUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    private final TelegramBot bot;
    private final TgUserService userService;

    public void serve() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();
        bot.execute(new SendMessage(message.chat().id(), "hello"));

    }
}
