package bibo.exception;

public abstract class BiboException extends Exception {
    public BiboException() {
        super("An error occurred.");
    }

    public BiboException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "BiboException: " + this.getMessage();
    }
}