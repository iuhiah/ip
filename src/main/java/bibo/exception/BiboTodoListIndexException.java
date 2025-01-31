package bibo.exception;

/**
 * Represents an exception that is thrown when an invalid index is used to access todo list.
 */
public class BiboTodoListIndexException extends BiboException {
    public BiboTodoListIndexException() {
        super("Invalid index.");
    }
    
    public BiboTodoListIndexException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "BiboTodoListInvalidIndexException: " + getMessage();
    }
}
