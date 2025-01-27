package bibo.exception;

public class BiboTaskDescriptionException extends BiboException {
    public BiboTaskDescriptionException() {
        super("Task description is invalid. Please provide a valid task description.");
    }

    public BiboTaskDescriptionException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "BiboTaskDescriptionException: " + this.getMessage();
    }
}