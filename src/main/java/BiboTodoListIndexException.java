public class BiboTodoListIndexException extends BiboException {
    public BiboTodoListIndexException() {
        super();
    }

    public BiboTodoListIndexException(String message) {
        super("Invalid index. Index: " + message);
    }

    @Override
    public String toString() {
        return "I can't find a task at that index. " + super.toString();
    }
}