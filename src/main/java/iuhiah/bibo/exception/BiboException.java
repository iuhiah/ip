package iuhiah.bibo.exception;

/**
 * Represents an exception that is thrown when an error occurs in the Bibo application.
 * It is an abstract class that is must be extended by more specific exception classes.
 */
public abstract class BiboException extends Exception {
    public BiboException() {
        super("An error occurred.");
    }

    public BiboException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "BiboException: " + getMessage();
    }
}
