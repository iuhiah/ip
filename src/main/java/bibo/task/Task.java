package bibo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task.
 */
public class Task {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
    private String description;
    private boolean isDone;

    /**
     * Creates a new task.
     *
     * @param description Description of task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Gets task description.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets task completion status to done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Sets task completion status to undone.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Formats datetime object to a string.
     *
     * @param dateTime
     * @return Formatted datetime string.
     */
    protected String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            Task task = (Task) obj;
            return this.description.equals(task.description);
        }
        return false;
    }

    /**
     * Returns task string in file format.
     */
    public String toFileString() {
        return this.toString();
    }

    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }
}
