package bibo.exception;

/**
 * Represents an exception that is thrown when an unknown command is provided.
 */
public class BiboUnknownCommandException extends BiboException {
    public BiboUnknownCommandException() {
        super("Unknown command. Please provide a valid command.");
    }

    public BiboUnknownCommandException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "BiboUnknownCommandException: " + getMessage();
    }
}