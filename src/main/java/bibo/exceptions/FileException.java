package bibo.exceptions;

/**
 * Represents an exception thrown when there is an error reading/writing to/from the task list file.
 */
public class FileException extends BiboException {
    public FileException() {
        super("Error reading/writing to task list file.");
    }

    public FileException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "FileException: " + getMessage();
    }
}
