package bibo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents a test class for Storage.
 */
public class TestStorage {
    private static Storage storage;
    private static String filePath;

    private void resetTestFile() throws Exception {
        Files.deleteIfExists(Paths.get(filePath));
        Files.createFile(Paths.get(filePath));
    }

    /**
     * Set up test files.
     */
    @BeforeAll
    public static void setupClass() throws Exception {
        System.out.println("Setting up Storage class tests.");
        storage = new Storage();

        Method method = Storage.class.getDeclaredMethod("getFilePath");
        method.setAccessible(true);
        filePath = (String) method.invoke(storage);

        System.out.println("Setup complete. Starting tests.");
    }

    /**
     * Clears saved data before each test.
     */
    @BeforeEach
    public void setup() {
        System.out.println("Clearing saved data.");

        try {
            resetTestFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Saved data cleared. Starting next test.");
    }

    /**
     * Cleans up test files after all tests are run.
     */
    @AfterAll
    public static void tearDownClass() {
        // remove test files
        try {
            Files.deleteIfExists(Paths.get(filePath));
            Files.deleteIfExists(Paths.get(filePath + ".corrupted"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        storage = null;
        filePath = null;
        System.out.println("Storage class tests complete.");
    }

    /**
     * Tests if hasSavedData creates a new file and returns false when file is not found.
     */
    @Test
    public void testHasSavedData_fileNotFound_createsNewFileAndReturnsFalse() throws Exception {
        // delete file to simulate file not found
        Files.deleteIfExists(Paths.get(filePath));

        Method method = Storage.class.getDeclaredMethod("hasSavedData");
        method.setAccessible(true);
        boolean hasSavedData = (boolean) method.invoke(storage);

        assertEquals(false, hasSavedData);
        assertEquals(true, Files.exists(Paths.get(filePath)));
    }

    /**
     * Tests if hasSavedData returns true when file is found.
     */
    @Test
    public void testHasSavedData_fileFound_returnsTrue()  throws Exception {
        Method method = Storage.class.getDeclaredMethod("hasSavedData");
        method.setAccessible(true);
        boolean hasSavedData = (boolean) method.invoke(storage);

        assertEquals(true, hasSavedData);
    }

    /**
     * Tests if checkCorruptedData creates a new file when data is corrupted.
     */
    @Test
    public void testCheckCorruptedData_corruptedData_createsNewFile() throws Exception {
        Method method = Storage.class.getDeclaredMethod("checkCorruptedData", int.class, int.class);
        method.setAccessible(true);

        // if data is corrupted, loadedTasks < totalTasks
        method.invoke(storage, 0, 1);

        assertEquals(true, Files.exists(Paths.get(filePath + ".corrupted")));
    }
}
