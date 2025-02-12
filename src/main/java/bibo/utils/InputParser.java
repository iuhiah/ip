package bibo.utils;

import java.util.Arrays;

import bibo.Command.CommandType;
import bibo.exceptions.ListIndexException;
import bibo.exceptions.NoteFormatException;
import bibo.exceptions.TaskFormatException;
import bibo.exceptions.UnknownCommandException;

/**
 * Represents a parser that parses inputs.
 */
public class InputParser {
    /**
     * Parses user input into command and arguments.
     *
     * @param input User input.
     * @return Parsed command and arguments.
     */
    public static String[] parseInput(String input) {
        String[] result = input.split(" ", 2);
        return result.length == 1
                ? new String[] { result[0], "" }
                : result;
    }

    /**
     * Parses task description from user input.
     *
     * @param cmd Task type.
     * @param input User input.
     * @return Parsed task description.
     * @throws TaskFormatException If task description format is invalid.
     * @throws UnknownCommandException If task type is unknown.
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
     * Parses task list index from user input.
     *
     * @param input User input.
     * @return Parsed task index.
     * @throws TaskFormatException If task index is invalid.
     */
    public static int parseTaskIndex(String input) throws ListIndexException {
        try {
            int index = Integer.parseInt(input);
            return index;
        } catch (NumberFormatException e) {
            throw new ListIndexException(
                ListIndexException.ErrorType.INVALID_INDEX.toString()
            );
        }
    }

    /**
     * Parses note description from user input.
     * If no content flag is present, whole input is considered as note description.
     *
     * @param input User input.
     * @return Parsed note description.
     * @throws NoteFormatException If note description format is invalid.
     */
    public static String[] parseNoteDescription(String input) throws NoteFormatException {
        if (input.isBlank()) {
            throw new NoteFormatException();
        }

        String[] parsedDescription = input.split(" /content ", 2);
        return parsedDescription.length == 1
                ? new String[] { parsedDescription[0], "" }
                : parsedDescription;
    }
}
