package bibo.task;

import java.time.LocalDateTime;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    
    @Override
    public String toFileString() {
        return "D" + super.toFileString() + " /by " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + super.formatDateTime(by) + ")";
    }
}