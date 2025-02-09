package bibo.task;

import java.time.LocalDateTime;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private LocalDateTime by;

    /**
     * Creates a new task with a deadline.
     *
     * @param description Description of task.
     * @param by Deadline of task.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Creates a new task with a deadline.
     *
     * @param description
     * @param dateTime
     */
    public Deadline(String description, LocalDateTime[] dateTime) {
        this(description, dateTime[0]);
    }

    /**
     * Gets task deadline.
     *
     * @return Task deadline.
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + super.formatDateTime(by) + ")";
    }
}
