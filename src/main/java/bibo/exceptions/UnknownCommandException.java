package bibo.exceptions;

/**
 * Represents an exception that is thrown when an unknown command is provided.
 */
public class UnknownCommandException extends BiboException {
    public UnknownCommandException() {
        super("Please provide a valid command.");
    }

    public UnknownCommandException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "UnknownCommandException: " + getMessage();
    }
}
