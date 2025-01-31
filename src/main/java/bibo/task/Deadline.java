package bibo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATE_TIME_FORMATTER
        = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
    private LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    
    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DATE_TIME_FORMATTER) + ")";
    }
}