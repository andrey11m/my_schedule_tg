package tg.com.my_schedule_tg.event;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tg.com.my_schedule_tg.service.TelegramBotService;

@Component
@RequiredArgsConstructor
public class TelegramEvent {

    private final TelegramBotService telegramBotService;

    @EventListener(ApplicationReadyEvent.class)
    public void runTelegramBot() {
        telegramBotService.serve();
    }
}
