package tg.com.my_schedule_tg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.com.my_schedule_tg.model.TgUser;

import java.util.Optional;

public interface TgUserRepo extends JpaRepository<TgUser, Long> {

}
