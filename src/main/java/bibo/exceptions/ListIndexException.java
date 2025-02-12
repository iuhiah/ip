package bibo.exceptions;

/**
 * Represents an exception that is thrown when an invalid index is used to access a list.
 */
public class ListIndexException extends BiboException {
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

    public ListIndexException() {
        super("Invalid index.");
    }

    public ListIndexException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "ListIndexException: " + getMessage();
    }
}
