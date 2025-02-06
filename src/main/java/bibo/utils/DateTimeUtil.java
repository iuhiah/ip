package bibo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date and time parsing.
 */
public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("[dd-MM-yyyy kkmm][yyyy-MM-dd'T'kk:mm]");

    /**
     * Parses date and time from string.
     *
     * @param dateTime Date and time string.
     * @return Parsed date and time.
     */
    public static LocalDateTime[] parseDateTime(String... dateTime) {
        LocalDateTime[] result = new LocalDateTime[dateTime.length];
        for (int i = 0; i < dateTime.length; i++) {
            result[i] = LocalDateTime.parse(dateTime[i], DATE_TIME_FORMATTER);
        }
        return result;
    }
}
