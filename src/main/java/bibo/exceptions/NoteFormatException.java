package bibo.exceptions;

/**
 * Represents an exception thrown when note format is invalid.
 */
public class NoteFormatException extends BiboException {
    public enum ErrorType {
        EMPTY {
            @Override
            public String toString() {
                return "Note format cannot be empty!";
            }
        },
        CONTENT_TOKEN {
            @Override
            public String toString() {
                return "Note format invalid!";
            }
        }
    }

    public NoteFormatException() {
        super("Note format cannot be empty.");
    }

    public NoteFormatException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "NoteFormatException: " + getMessage();
    }
}
