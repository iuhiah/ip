package bibo.exception;

/**
 * Represents an exception thrown when there is an error reading/writing to/from the todo list file.
 */
public class BiboTodoListFileException extends BiboException {
    public BiboTodoListFileException() {
        super("Error reading/writing to todo list file.");
    }

    public BiboTodoListFileException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "BiboTodoListFileException: " + this.getMessage();
    }    
}
