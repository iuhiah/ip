package bibo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import bibo.Command.CommandType;
import bibo.exceptions.TaskFormatException;
import bibo.exceptions.TaskListIndexException;
import bibo.exceptions.UnknownCommandException;

/**
 * Represents a parser that parses inputs.
 */
public class InputParser {
    /**
     * Checks if given command is a valid command.
     *
     * @return enum value of valid command.
     * @throws UnknownCommandException If command is not valid.
     */
    public static String[] parseInput(String input) {
        String[] result = input.split(" ", 2);
        return result.length == 1
            ? new String[] { result[0], "" }
            : result;
    }

    /**
     * Parses task description from user input.
     * Parsed data will be used to create a new task.
     *
     * @param input User input.
     * @return Parsed arguments for task description.
     * @throws TaskFormatException If task description is invalid.
     */
    public static String[] parseTaskDescription(CommandType cmd, String input)
            throws TaskFormatException, UnknownCommandException {
        if (input.isBlank()) {
            throw new TaskFormatException(
                TaskFormatException.ErrorType.EMPTY.toString()
            );
        }

        String[] args = new String[] { input };

        switch (cmd) {
        case TODO:
            break;
        case DEADLINE:
            args = input.split(" /by ");
            if (args.length != 2 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new TaskFormatException(
                    TaskFormatException.ErrorType.MISSING_DEADLINE_TOKEN.toString()
                );
            }
            break;
        case EVENT:
            args = input.split(" /from | /to ");
            if (args.length != 3 || Arrays.stream(args).anyMatch(String::isBlank)) {
                throw new TaskFormatException(
                    TaskFormatException.ErrorType.MISSING_EVENT_TOKEN.toString()
                );
            }
            break;
        default:
            throw new TaskFormatException(
                TaskFormatException.ErrorType.UNKNOWN_TASK_TYPE.toString()
            );
        }

        return args;
    }

    /**
     * Parses task date time from user input.
     *
     * @param args Date/time arguments.
     * @return Parsed date time.
     * @throws TaskFormatException If date time format is invalid.
     */
    protected static LocalDateTime[] parseTaskDateTime(String[] dateTimes) throws TaskFormatException {
        LocalDateTime[] dateTime;
        // todo: add support for more date time formats
        try {
            dateTime = Arrays.stream(dateTimes)
                .map(DateTimeUtil::parseDateTime)
                .toArray(LocalDateTime[]::new);
        } catch (DateTimeParseException e) {
            throw new TaskFormatException(
                TaskFormatException.ErrorType.DATE_TIME_INVALID.toString()
            );
        }
        return dateTime;
    }

    /**
     * Parses task list index from user input.
     *
     * @param input User input.
     * @return Parsed task index.
     * @throws TaskFormatException If task index is invalid.
     */
    public static int parseTaskIndex(String input) throws TaskListIndexException {
        try {
            int index = Integer.parseInt(input);
            return index;
        } catch (NumberFormatException e) {
            throw new TaskListIndexException(
                TaskListIndexException.ErrorType.INVALID_INDEX.toString()
            );
        }
    }
}
