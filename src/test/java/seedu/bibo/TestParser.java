package seedu.bibo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bibo.Command;
import bibo.Parser;
import bibo.exception.BiboTaskDescriptionException;

/**
 * Represents a test class for Parser.
 */
public class TestParser {
    private static Command cmd;
    private static Parser parser;

    @BeforeAll
    public static void setupClass() {
        System.out.println("Setting up Parser class tests.");
        parser = new Parser();
        cmd = new Command(null, null);

        try {
            Method setCommand = Parser.class.getDeclaredMethod("setCommand", Command.class);
            setCommand.setAccessible(true);
            setCommand.invoke(parser, cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Setup complete. Starting tests.");
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
        parser = null;
        cmd = null;
        System.out.println("All tests completed.");
    }

    /**
     * Tests if exception is thrown when task description is empty.
     */
    @Test
    public void testParseTaskDescription_emptyDescription_exceptionThrown() {
        try {
            Method setCommandType = Command.class.getDeclaredMethod("setCommandType", String.class);
            setCommandType.setAccessible(true);

            Method parseTaskDescription = Parser.class.getDeclaredMethod("parseTaskDescription", String.class);
            parseTaskDescription.setAccessible(true);

            setCommandType.invoke(cmd, "todo");
            try {
                parseTaskDescription.invoke(parser, "");
            } catch (Exception e) {
                assertEquals(e.getCause().getClass(), BiboTaskDescriptionException.class);
            }

            setCommandType.invoke(cmd, "deadline");
            try {
                parseTaskDescription.invoke(parser, " /by ");
            } catch (Exception e) {
                assertEquals(e.getCause().getClass(), BiboTaskDescriptionException.class);
            }

            setCommandType.invoke(cmd, "event");
            try {
                parseTaskDescription.invoke(parser, " /from /to ");
            } catch (Exception e) {
                assertEquals(e.getCause().getClass(), BiboTaskDescriptionException.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if task description is parsed correctly.
     */
    @Test
    public void testParseTaskDescription_validDescription_parsedCorrectly() {
        try {
            Method setCommandType = Command.class.getDeclaredMethod("setCommandType", String.class);
            setCommandType.setAccessible(true);

            Method parseTaskDescription = Parser.class.getDeclaredMethod("parseTaskDescription", String.class);
            parseTaskDescription.setAccessible(true);

            String validDescription = "description";
            assertEquals(parseTaskDescription.invoke(parser, validDescription),
                new String[] { validDescription });

            setCommandType.invoke(cmd, "deadline");
            assertEquals(
                parseTaskDescription.invoke(parser, validDescription + "survey /by 2021-12-31 2359"),
                new String[] { validDescription, "2021-12-31 2359" }
            );

            setCommandType.invoke(cmd, "event");
            assertEquals(
                parseTaskDescription.invoke(parser, validDescription + " /from 2021-12-31 2359 /to 2022-01-01 0000"),
                new String[] { validDescription, "2021-12-31 2359", "2022-01-01 0000" }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if exception is thrown when datetime format is invalid.
     */
    @Test
    public void testParseTaskDescription_invalidDateTime_exceptionThrown() {
        try {
            Method setCommandType = Command.class.getDeclaredMethod("setCommandType", String.class);
            setCommandType.setAccessible(true);

            Method parseDateTime = Parser.class.getDeclaredMethod("parseDateTime", String.class);
            parseDateTime.setAccessible(true);

            String invalidDateTime = "9999-99-99 99:99";
            setCommandType.invoke(cmd, "deadline");
            try {
                parseDateTime.invoke(parser, invalidDateTime);
            } catch (Exception e) {
                assertEquals(e.getCause().getClass(), BiboTaskDescriptionException.class);
            }

            setCommandType.invoke(cmd, "event");
            try {
                parseDateTime.invoke(parser, invalidDateTime);
            } catch (Exception e) {
                assertEquals(e.getCause().getClass(), BiboTaskDescriptionException.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
