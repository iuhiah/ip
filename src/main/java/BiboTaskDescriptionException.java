public class BiboTaskDescriptionException extends BiboException {
    public BiboTaskDescriptionException() {
        super();
    }

    public BiboTaskDescriptionException(String message) {
        super("Invalid task description. Task type: " + message);
    }

    @Override
    public String toString() {
        return "That's not a valid task description. " + super.toString();
    }
}