package bibo;

import java.util.Arrays;
import bibo.exception.BiboUnknownCommandException;
import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;

/**
 * Represents a parser that parses inputs.
 */
public class Parser {
    protected enum ValidCommands {
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
    protected static ValidCommands checkValidCommand(String cmd) throws BiboUnknownCommandException {
        try {
            return ValidCommands.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BiboUnknownCommandException();
        }
    }

    /**
     * Parses task description from user input.
     * Parsed data will be used to create a new task.
     * 
     * @param input User input.
     * @return Parsed arguments for task description.
     * @throws BiboTaskDescriptionException If task description is invalid.
     */
    protected static String[] parseTaskDescription(ValidCommands cmd, String input) throws BiboTaskDescriptionException {
        if (input.isBlank()) {
            throw new BiboTaskDescriptionException("Task description empty!");
        }

        String[] args = new String[] { input };

        if (cmd == ValidCommands.DEADLINE) {    
            args = input.split(" /by ");
            if (args.length != 2 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new BiboTaskDescriptionException("Deadline description format invalid!");
            }
        } else if (cmd == ValidCommands.EVENT) {
            args = input.split(" /from | /to ");
            if (args.length != 3 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new BiboTaskDescriptionException("Event description format invalid!");
            }
        }
        return args;
    }

    /**
     * Parses task description from saved data.
     * 
     * @param taskType
     * @param input
     * @return
     * @throws BiboTaskDescriptionException
     */
    protected static String parseTaskDescription(char taskType, String input) throws BiboTaskDescriptionException {
        switch (taskType) {
        case 'T':
        break;
        case 'D':
            input = input.replace(" (by: ", " /by ")
                            .replace(")", "");
            break;
        case 'E':
            input = input.replace(" (from: ", " /from ")
                            .replace(" to: ", " /to ")
                            .replace(")", "");
            break;
        }
        return input;
    }

    /**
     * Parses todo list index from user input.
     * 
     * @param input User input.
     * @return Parsed task index.
     * @throws BiboTaskDescriptionException If task index is invalid.
     */
    protected static int parseTaskIndex(String input) throws BiboTodoListIndexException {
        try {
            int index = Integer.parseInt(input);
            return index;
        } catch (NumberFormatException e) {
            throw new BiboTodoListIndexException("That's not a number!");
        }
    }
}
