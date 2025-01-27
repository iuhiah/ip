package bibo;

import java.util.Arrays;
import bibo.exception.BiboUnknownCommandException;
import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;

/**
 * Represents a parser that parses user inputs.
 */
public class Parser {
    public enum ValidCommands {
        BYE, LIST,
        TODO, DEADLINE, EVENT,
        MARK, UNMARK, DELETE
    }

    /**
     * Checks if given command is a valid command.
     * 
     * @param cmd Command to check.
     * @return enum value of valid command.
     * @throws BiboUnknownCommandException If command is not valid.
     */
    public static ValidCommands checkValidCommand(String cmd) throws BiboUnknownCommandException {
        try {
            return ValidCommands.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BiboUnknownCommandException(cmd);
        }
    }

    /**
     * Parses task description from user input.
     * 
     * @param input User input.
     * @return Parsed arguments for task description.
     * @throws BiboTaskDescriptionException If task description is invalid.
     */
    public static String[] parseTaskDescription(ValidCommands cmd, String input) throws BiboTaskDescriptionException {
        if (input.isBlank()) {
            throw new BiboTaskDescriptionException("Task description cannot be empty.");
        }

        String[] args = new String[] { input };

        if (cmd == ValidCommands.DEADLINE) {    
            args = input.split(" /by ");
            if (args.length != 2 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new BiboTaskDescriptionException("Deadline description format invalid. Use: deadline <task> /by <deadline>");
            }
        } else if (cmd == ValidCommands.EVENT) {
            args = input.split(" /from | /to ");
            if (args.length != 3 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new BiboTaskDescriptionException("Event description format invalid. Use: event <task> /from <start> /to <end>");
            }
        }
        return args;
    }

    /**
     * Parses todo list index from user input.
     * 
     * @param input User input.
     * @return Parsed task index.
     * @throws BiboTaskDescriptionException If task index is invalid.
     */
    public static int parseTaskIndex(String input) throws BiboTodoListIndexException {
        try {
            int index = Integer.parseInt(input);
            return index;
        } catch (NumberFormatException e) {
            throw new BiboTodoListIndexException("That's not a number.");
        }
    }
}
