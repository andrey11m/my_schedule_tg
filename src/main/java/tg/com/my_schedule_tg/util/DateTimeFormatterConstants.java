package tg.com.my_schedule_tg.util;

import java.time.format.DateTimeFormatter;

public class DateTimeFormatterConstants {

    public static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter.ofPattern("HH-mm dd-MM yyyy");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DAY_OF_WEEK_FORMATTER = DateTimeFormatter.ofPattern("HH:mm E");


    public DateTimeFormatterConstants() {
    }
}
