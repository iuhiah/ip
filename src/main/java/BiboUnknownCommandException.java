public class BiboUnknownCommandException extends BiboException {
    public BiboUnknownCommandException() {
        super();
    }

    public BiboUnknownCommandException(String message) {
        super("Unknown command.");
    }

    @Override
    public String toString() {
        return "I don't recognise that command. " + super.toString();
    }
}