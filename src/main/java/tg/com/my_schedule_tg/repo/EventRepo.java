package tg.com.my_schedule_tg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.com.my_schedule_tg.model.Event;

import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {

    List<Event> findEventsByUserId(Long userId);
}
