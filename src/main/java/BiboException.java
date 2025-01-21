public abstract class BiboException extends Exception {
    public BiboException() {
        super();
    }

    public BiboException(String message) {
        super("Bibo: " + message);
    }

    @Override
    public String toString() {
        return "Try again!";
    }
}