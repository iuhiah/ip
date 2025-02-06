package bibo.exceptions;

/**
 * Represents an exception that is thrown when the task description format is invalid.
 */
public class TaskFormatException extends BiboException {
    /**
     * Represents the type of error that occurred.
     */
    public enum ErrorType {
        EMPTY {
            @Override
            public String toString() {
                return "Task description is empty!";
            }
        },
        MISSING_DEADLINE_TOKEN {
            @Override
            public String toString() {
                return "Deadline format invalid!"
                        + "Format: deadline <description> /by <date time>";
            }
        },
        MISSING_EVENT_TOKEN {
            @Override
            public String toString() {
                return "Event format invalid!"
                        + "Format: event <description> /from <start date time> /to <end date time>";
            }
        },
        DATE_TIME_INVALID {
            @Override
            public String toString() {
                return "Date and time format invalid!";
            }
        },
        UNKNOWN_TASK_TYPE {
            @Override
            public String toString() {
                return "Unknown task type!";
            }
        }
    }

    public TaskFormatException() {
        super("Task format is invalid.");
    }

    public TaskFormatException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "TaskFormatException: " + getMessage();
    }
}
