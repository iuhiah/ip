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
        DateTimeFormatter.ofPattern("[MM-dd-yyyy kkmm][yyyy-MM-dd'T'HH:mm]");

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
            System.out.println(args[1]);
            throw new BiboTaskDescriptionException("Invalid date/time format!");
        }
        return dateTime;
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
        String[] parsedInput;
        switch (taskType) {
        case 'T':
            break;
        case 'D':
            parsedInput = input.split(" \\| ");
            if (parsedInput.length != 2) {
                throw new BiboTaskDescriptionException("Invalid deadline task data!");
            }
            input = parsedInput[0] + " /by " + parsedInput[1];
            break;
        case 'E':
            parsedInput = input.split(" \\| ");
            if (parsedInput.length != 3) {
                throw new BiboTaskDescriptionException("Invalid event task data!");
            }
            input = parsedInput[0] + " /from " + parsedInput[1] + " /to " + parsedInput[2];
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
