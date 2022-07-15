package tg.com.my_schedule_tg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyScheduleTgApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyScheduleTgApplication.class, args);
    }

}
