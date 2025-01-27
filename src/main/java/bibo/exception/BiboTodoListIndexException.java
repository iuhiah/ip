package bibo.exception;

public class BiboTodoListIndexException extends BiboException {
    public BiboTodoListIndexException() {
        super("Invalid index.");
    }
    
    public BiboTodoListIndexException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "BiboTodoListInvalidIndexException: " + this.getMessage();
    }
}
