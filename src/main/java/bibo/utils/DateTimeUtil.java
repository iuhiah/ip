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
    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }

    /**
     * Parses date and time from array of strings.
     *
     * @param dateTimes Array of date and time strings.
     * @return Parsed date and time.
     */
    public static LocalDateTime[] parseDateTime(String[] dateTimes) {
        LocalDateTime[] dateTime = new LocalDateTime[dateTimes.length];

        for (int i = 0; i < dateTimes.length; i++) {
            dateTime[i] = parseDateTime(dateTimes[i]);
        }

        return dateTime;
    }
}
