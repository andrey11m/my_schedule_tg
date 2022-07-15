package tg.com.my_schedule_tg.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tg.com.my_schedule_tg.repo.EventRepo;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final TelegramBot bot;
    private final EventRepo eventRepo;
    private final EventService eventService;

    private final String EVERY_DAY_TIME = "0 30 8 * * *";
    private final String EVERY_MINUTE = "PT20S";

    @Scheduled(fixedRateString = EVERY_MINUTE)
    private void remind10MinutesBefore() {
        eventRepo.findAll().forEach(event -> {
            if (!LocalDateTime.now().plusMinutes(10).isBefore(event.getDateTime()) && !event.getState().equals("sent")) {
                bot.execute(eventService.getSendMessage(event));
                event.setState("sent");
                eventRepo.save(event);
            }
            if (event.getDateTime().isBefore(LocalDateTime.now())) {
                eventRepo.delete(event);
            }
        });
    }

    @Scheduled(cron = EVERY_DAY_TIME)
    private void remindDayBefore() {
        eventRepo.findAll().stream()
                .filter(event -> LocalDateTime.now().getMonthValue() == event.getDateTime().getMonthValue())
                .forEach(event -> bot.execute(eventService.getSendMessage(event)));
    }

}
