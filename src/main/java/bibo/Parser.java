package bibo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import bibo.exception.BiboUnknownCommandException;
import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;

/**
 * Represents a parser that parses inputs.
 */
public class Parser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("[dd-MM-yyyy kkmm][yyyy-MM-dd'T'kk:mm]");

    protected enum Commands {
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
    protected static Commands checkValidCommand(String cmd) throws BiboUnknownCommandException {
        try {
            return Commands.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BiboUnknownCommandException();
        }
    }

    /**
     * Gets task type from user input (file data).
     * 
     * @param cmd User input.
     * @return Task type.
     * @throws BiboUnknownCommandException If task type is invalid.
     */
    protected static Commands getTaskType(char cmd) throws BiboUnknownCommandException {
        switch (cmd) {
        case 'T':
            return Commands.TODO;
        case 'D':
            return Commands.DEADLINE;
        case 'E':
            return Commands.EVENT;
        default:
            throw new BiboUnknownCommandException("Unknown task type in file data!");
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
    protected static String[] parseTaskDescription(Commands cmd, String input) throws BiboTaskDescriptionException {
        if (input.isBlank()) {
            throw new BiboTaskDescriptionException("Task description empty!");
        }

        String[] args = new String[] { input };

        if (cmd == Commands.DEADLINE) {    
            args = input.split(" /by ");
            if (args.length != 2 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new BiboTaskDescriptionException("Deadline description format invalid!");
            }

        } else if (cmd == Commands.EVENT) {
            args = input.split(" /from | /to ");
            if (args.length != 3 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new BiboTaskDescriptionException("Event description format invalid!");
            }
        }
        return args;
    }

    /**
     * Parses task date time from user input.
     * 
     * @param args Date/time arguments.
     * @return Parsed date time.
     * @throws BiboTaskDescriptionException If date time format is invalid.
     */
    protected static LocalDateTime[] parseTaskDateTime(String[] args) throws BiboTaskDescriptionException {
        LocalDateTime[] dateTime = new LocalDateTime[2];

        // todo: add support for more date time formats
        try {
            if (args.length == 2) {
                dateTime[0] = LocalDateTime.parse(args[1], DATE_TIME_FORMATTER);
            } else {
                dateTime[0] = LocalDateTime.parse(args[1], DATE_TIME_FORMATTER);
                dateTime[1] = LocalDateTime.parse(args[2], DATE_TIME_FORMATTER);
            }
        } catch (Exception e) {
            throw new BiboTaskDescriptionException("Invalid date/time format!");
        }
        return dateTime;
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
