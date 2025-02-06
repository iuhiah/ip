package bibo.task;

import java.time.LocalDateTime;

/**
 * Represents an event.
 */
public class Event extends Task {
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Creates a new event.
     *
     * @param description Description of event.
     * @param start Start time of event.
     * @param end End time of event.
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Creates a new event.
     *
     * @param description Description of event.
     * @param dateTime Date and time details of event.
     */
    public Event(String description, LocalDateTime[] dateTime) {
        this(description, dateTime[0], dateTime[1]);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
            + " (from: " + super.formatDateTime(start)
            + " to: " + super.formatDateTime(end) + ")";
    }
}
