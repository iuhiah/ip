package bibo.exceptions;

/**
 * Represents an exception that is thrown when an invalid index is used to access task list.
 */
public class TaskListIndexException extends BiboException {
    /**
     * Represents the type of error that occurred.
     */
    public enum ErrorType {
        INVALID_INDEX {
            @Override
            public String toString() {
                return "Invalid index!";
            }
        },
        INDEX_OUT_OF_BOUNDS {
            @Override
            public String toString() {
                return "Index out of bounds!";
            }
        }
    }

    public TaskListIndexException() {
        super("Invalid index.");
    }

    public TaskListIndexException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "TaskListIndexException: " + getMessage();
    }
}
