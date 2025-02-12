package bibo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bibo.Command.CommandType;
import bibo.exceptions.TaskFormatException;
import bibo.utils.InputParser;

/**
 * Represents a test class for InputParser.
 */
public class TestInputParser {
    @BeforeAll
    public static void setupClass() {
        System.out.println("Starting InputParser tests.");
    }

    @BeforeEach
    public void setup() {
        System.out.println("Starting next test.");
    }

    /**
     * Cleans up test files after all tests are run.
     */
    @AfterAll
    public static void tearDownClass() {
        System.out.println("All tests completed.");
    }

    /**
     * Tests if exception with correct error type is thrown when task description is empty.
     */
    @Test
    public void testParseTaskDescription_emptyDescription_exceptionThrown() {
        assertThrows(TaskFormatException.class, () -> {
            InputParser.parseTaskDescription(CommandType.TODO, "");
        }, TaskFormatException.ErrorType.EMPTY.toString());

        assertThrows(TaskFormatException.class, () -> {
            InputParser.parseTaskDescription(CommandType.DEADLINE, "");
        }, TaskFormatException.ErrorType.EMPTY.toString());

        assertThrows(TaskFormatException.class, () -> {
            InputParser.parseTaskDescription(CommandType.EVENT, "");
        }, TaskFormatException.ErrorType.EMPTY.toString());
    }

    /**
     * Tests if exception with correct error type is thrown when task description is invalid.
     */
    @Test
    public void testParseTaskDescription_invalidDescription_exceptionThrown() {
        assertThrows(TaskFormatException.class, () -> {
            InputParser.parseTaskDescription(CommandType.DEADLINE, "description");
        }, TaskFormatException.ErrorType.MISSING_DEADLINE_TOKEN.toString());

        assertThrows(TaskFormatException.class, () -> {
            InputParser.parseTaskDescription(CommandType.EVENT, "description");
        }, TaskFormatException.ErrorType.MISSING_EVENT_TOKEN.toString());

        assertThrows(TaskFormatException.class, () -> {
            InputParser.parseTaskDescription(CommandType.EVENT, "description /from ");
        }, TaskFormatException.ErrorType.MISSING_EVENT_TOKEN.toString());

        assertThrows(TaskFormatException.class, () -> {
            InputParser.parseTaskDescription(CommandType.EVENT, "description /to ");
        }, TaskFormatException.ErrorType.MISSING_EVENT_TOKEN.toString());
    }

    /**
     * Tests if task description is parsed correctly.
     */
    @Test
    public void testParseTaskDescription_validDescription_parsedCorrectly() {
        try {
            String validDescription = "description";
            assertEquals(true, Arrays.equals(
                InputParser.parseTaskDescription(CommandType.TODO, validDescription),
                new String[] { validDescription }
            ));

            assertEquals(true, Arrays.equals(
                InputParser.parseTaskDescription(CommandType.DEADLINE,
                        validDescription + " /by 2021-12-31 2359"),
                new String[] { validDescription, "2021-12-31 2359" }
            ));

            assertEquals(true, Arrays.equals(
                InputParser.parseTaskDescription(CommandType.EVENT,
                        validDescription + " /from 2021-12-31 2359 /to 2022-01-01 0000"),
                new String[] { validDescription, "2021-12-31 2359", "2022-01-01 0000" }
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if note description is parsed correctly.
     */
    @Test
    public void testParseNoteDescription_validDescription_parsedCorrectly() {
        try {
            // note without title
            assertArrayEquals(
                new String[] { "description", "" },
                InputParser.parseNoteDescription("description")
            );

            // note with title
            assertArrayEquals(
                new String[] { "title", "description" },
                InputParser.parseNoteDescription("title /content description")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
