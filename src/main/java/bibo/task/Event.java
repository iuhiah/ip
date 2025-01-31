package bibo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event.
 */
public class Event extends Task {
    private static final DateTimeFormatter DATE_TIME_FORMATTER
        = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
    private LocalDateTime start;
    private LocalDateTime end;

    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toFileString() {
        return "E" + super.toFileString() + " /from " + start + " /to " + end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
            + " (from: " + start.format(DATE_TIME_FORMATTER)
            + " to: " + end.format(DATE_TIME_FORMATTER) + ")";
    }
}