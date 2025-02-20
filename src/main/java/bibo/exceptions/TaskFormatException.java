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
        UNKNOWN_TASK_TYPE {
            @Override
            public String toString() {
                return "Unknown task type!";
            }
        },
        DEADLINE_TOKEN {
            @Override
            public String toString() {
                return "Deadline format invalid!";
            }
        },
        EVENT_TOKEN {
            @Override
            public String toString() {
                return "Event format invalid!";
            }
        },
        DATE_TIME_INVALID {
            @Override
            public String toString() {
                return "Start date and time must be before end date and time!";
            }
        },
        MISSING_ARGUMENT {
            @Override
            public String toString() {
                return "Missing argument! Check task format.";
            }
        },
        DUPLICATE_TASK {
            @Override
            public String toString() {
                return "Task already exists!";
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
