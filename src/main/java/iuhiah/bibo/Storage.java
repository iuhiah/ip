package iuhiah.bibo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import iuhiah.bibo.exception.BiboTodoListFileException;

/**
 * Represents a file handler that handles file operations.
 */
public class Storage {
    // define file separator depending on OS
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private String dataDir = "data";
    private String fileName = "todo.txt";

    private String getFilePath() {
        return dataDir + FILE_SEPARATOR + fileName;
    }

    /*
     * Checks if todo list file exists. If not, creates one.
     *
     * @throws BiboTodoListFileException if an error occurs while creating the file.
     */
    protected boolean hasSavedData() throws BiboTodoListFileException {
        System.out.println("Checking for saved data...");

        try {
            if (!Files.exists(Paths.get(getFilePath()))) {
                System.out.println("Saved data not found. Creating new file to store data...");
                Files.createDirectory(Paths.get(dataDir));
                Files.createFile(Paths.get(getFilePath()));
                return false;
            }
            return true;
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error creating todo list file.");
        }
    }

    /**
     * Loads data from file.
     *
     * @param bibo Bibo instance to load todo list into.
     * @throws BiboTodoListFileException if unable to read from file.
     */
    protected String[] loadTodoList() throws BiboTodoListFileException {
        System.out.println("Saved data found. Loading data from file...");
        String[] taskData = null;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()))) {
            taskData = fileReader.lines().toArray(String[]::new);
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error reading from todo list file.");
        }

        return taskData;
    }

    /**
     * Provides progress update on reading from file.
     * Separates corrupted and non-corrupted data.
     *
     * @param bibo Bibo instance to save todo list from.
     * @param totalTasks Total number of tasks in file.
     * @throws BiboTodoListFileException if an error occurs during file handling.
     */
    protected void checkCorruptedData(int loadedTasks, int totalTasks) throws BiboTodoListFileException {
        System.out.println("Successfully loaded " + loadedTasks + " of " + totalTasks + " tasks.");

        if (loadedTasks < totalTasks) {
            System.out.println("Some data was corrupted, corrupted data file renamed to:");
            System.out.println("\t" + getFilePath() + ".corrupted");

            try {
                File corruptedFile = new File(getFilePath());
                corruptedFile.renameTo(new File(getFilePath() + ".corrupted"));
            } catch (NullPointerException | SecurityException e) {
                throw new BiboTodoListFileException("Error renaming corrupted file.");
            }
        }
    }

    /**
     * Saves current todo list to file.
     *
     * @param bibo Bibo instance to save todo list from.
     * @throws BiboTodoListFileException if an error occurs while saving the file.
     */
    protected void saveTodoList(TaskList todoList) throws BiboTodoListFileException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(getFilePath()))) {
            writer.write(todoList.toFileString());
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error saving todo list file.");
        }
    }
}
