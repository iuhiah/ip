package bibo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;
import bibo.exception.BiboUnknownCommandException;

/**
 * Represents a parser that parses inputs.
 */
public class Parser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("[dd-MM-yyyy kkmm][yyyy-MM-dd'T'kk:mm]");
    private static Command cmd;

    /**
     * Sets command to be used for parsing.
     *
     * @param command Command to set.
     */
    protected static void setCommand(Command command) {
        cmd = command;
    }

    /**
     * Checks if given command is a valid command.
     *
     * @param cmd Command to check.
     * @return enum value of valid command.
     * @throws BiboUnknownCommandException If command is not valid.
     */
    protected static String parseInput(String input) throws BiboUnknownCommandException {
        try {
            String[] inputSplit = input.split(" ", 2);

            String cmdString = inputSplit[0];
            cmd.setCommandType(cmdString);

            return inputSplit.length > 1 ? inputSplit[1] : "";
        } catch (BiboUnknownCommandException e) {
            throw e;
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
    protected static String[] parseTaskDescription(String input) throws BiboTaskDescriptionException {
        if (input.isBlank()) {
            throw new BiboTaskDescriptionException("Task description empty!");
        }

        String[] args = new String[] { input };

        switch (cmd.getCommandType()) {
        case TODO:
            break;
        case DEADLINE:
            args = input.split(" /by ");
            if (args.length != 2 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new BiboTaskDescriptionException("Deadline description format invalid!");
            }
            break;
        case EVENT:
            args = input.split(" /from | /to ");
            if (args.length != 3 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new BiboTaskDescriptionException("Event description format invalid!");
            }
            break;
        default:
            throw new BiboTaskDescriptionException("Error parsing command!");
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
            switch (cmd.getCommandType()) {
            case DEADLINE:
                dateTime[0] = LocalDateTime.parse(args[1], DATE_TIME_FORMATTER);
                break;
            case EVENT:
                dateTime[0] = LocalDateTime.parse(args[1], DATE_TIME_FORMATTER);
                dateTime[1] = LocalDateTime.parse(args[2], DATE_TIME_FORMATTER);
                break;
            default:
                throw new BiboTaskDescriptionException();
            }
        } catch (DateTimeParseException | BiboTaskDescriptionException e) {
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
