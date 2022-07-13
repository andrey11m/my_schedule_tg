package tg.com.my_schedule_tg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.com.my_schedule_tg.model.TgUser;
import tg.com.my_schedule_tg.repo.TgUserRepo;

@Service
@RequiredArgsConstructor
public class TgUserService {

    private final TgUserRepo userRepo;


    public void addEvent(long chatId) {
        TgUser user = TgUser.builder().id(chatId).build();
        userRepo.findById(chatId).ifPresentOrElse(tgUser -> System.out.println("Found in db"),
                () -> userRepo.save(user));
    }
}
