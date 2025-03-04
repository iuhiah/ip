package bibo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bibo.exceptions.TaskFormatException;
import bibo.utils.FileParser;

/**
 * Represents a test class for FileParser.
 */
public class TestFileParser {
    @BeforeAll
    public static void setupClass() {
        System.out.println("Starting FileParser tests.");
    }

    @BeforeEach
    public void setup() {
        System.out.println("Starting next test.");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("All tests completed.");
    }

    /**
     * Tests if exception is thrown when task type is unknown.
     */
    @Test
    public void testParseTaskData_unknownTaskType_exceptionThrown() {
        assertThrows(TaskFormatException.class, () -> {
            FileParser.parseTaskData("[X][X] read book");
        });
    }


    /**
     * Tests if file data is parsed correctly.
     * Test cases: Each task typ, one marked, one unmarked.
    * @throws TaskFormatException
    */
    @Test
    public void testParseTaskData() throws TaskFormatException {
        assertArrayEquals(
            FileParser.parseTaskData("[T][X] read book"),
            new String[] { "todo", "read book", "true" }
        );
        assertArrayEquals(
            FileParser.parseTaskData("[T][ ] read another book"),
            new String[] { "todo", "read another book", "false" }
        );

        assertArrayEquals(
            FileParser.parseTaskData("[D][X] return book /by xxx"),
            new String[] { "deadline", "return book /by xxx", "true" }
        );
        assertArrayEquals(
            FileParser.parseTaskData("[D][ ] return another book /by xxx"),
            new String[] { "deadline", "return another book /by xxx", "false" }
        );

        assertArrayEquals(
            FileParser.parseTaskData("[E][X] project meeting /from xxx /to yyy"),
            new String[] { "event", "project meeting /from xxx /to yyy", "true" }
        );
        assertArrayEquals(
            FileParser.parseTaskData("[E][ ] another project meeting /from xxx /to yyy"),
            new String[] { "event", "another project meeting /from xxx /to yyy", "false" }
        );
    }
}
