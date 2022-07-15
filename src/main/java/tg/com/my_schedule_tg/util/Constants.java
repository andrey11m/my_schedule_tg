package tg.com.my_schedule_tg.util;

public class Constants {
    // Commands
    public static final String NEW_EVENT = "/newevent";
    public static final String ALL_EVENTS = "/allevents";
    public static final String WEEK_EVENTS = "/week";

    // messages

    public static final String NO_EVENTS = "You have no events \uD83E\uDD37\u200D♂️";
    public static final String NEW_EVENT_ENTER_NAME_MESSAGE = "Enter the name of event";
    public static final String NEW_EVENT_ENTER_DATE_MESSAGE = "Enter the date of event in format \"13-40 28-08\"";
    public static final String NEW_EVENT_ENTER_REPEAT_MESSAGE = "Do you want to repeat this event?";
    public static final String NEW_EVENT_CREATED_MESSAGE = "Event created";
    public static final String STATUS_CALLBACK_DATE = "removed";

    // event status
    public static final String NAME_STATUS = "name";
    public static final String COMPLETED_STATUS = "completed";
    public static final String REPEATABLE_STATUS = "repeatable";
    public static final String DATE_STATUS = "date";
    private Constants() {
    }
}
