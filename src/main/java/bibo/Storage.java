package bibo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import bibo.exceptions.FileException;
import bibo.task.TaskList;

/**
 * Represents a file handler that handles file operations.
 */
public class Storage {
    // define file separator depending on OS
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private String dataDir = "data";
    private String fileName = "tasks.txt";

    private String getFilePath() {
        return dataDir + FILE_SEPARATOR + fileName;
    }

    /*
     * Checks if task list file exists. If not, creates one.
     *
     * @throws BiboTaskListFileException if an error occurs while creating the file.
     */
    protected boolean hasSavedData() throws FileException {
        System.out.println("Checking for saved data...");

        if (!Files.exists(Paths.get(dataDir))) {
            System.out.println("Data directory not found. Creating new directory to store data...");
            try {
                Files.createDirectory(Paths.get(dataDir));
            } catch (IOException e) {
                throw new FileException();
            }
        }

        if (!Files.exists(Paths.get(getFilePath()))) {
            System.out.println("Saved data not found. Creating new file to store data...");

            try {
                Files.createFile(Paths.get(getFilePath()));
            } catch (IOException e) {
                throw new FileException();
            }

            return false;
        }

        return true;
    }

    /**
     * Gets task data from file.
     *
     * @param bibo Bibo instance to load task list into.
     * @throws FileException if unable to read from file.
     */
    protected String[] getTaskData() throws FileException {
        if (!hasSavedData()) {
            return null;
        }

        System.out.println("Saved data found. Loading data from file...");
        String[] taskData = null;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()))) {
            taskData = fileReader.lines().toArray(String[]::new);
        } catch (IOException e) {
            throw new FileException();
        }

        return taskData;
    }

    /**
     * Provides progress update on reading from file.
     * Separates corrupted and non-corrupted data.
     *
     * @param bibo Bibo instance to save task list from.
     * @param totalTasks Total number of tasks in file.
     * @throws FileException if an error occurs during file handling.
     */
    protected void checkCorruptedData(int loadedTasks, int totalTasks) throws FileException {
        System.out.println("Successfully loaded " + loadedTasks + " of " + totalTasks + " tasks.");

        if (loadedTasks < totalTasks) {
            System.out.println("Some data was corrupted, corrupted data file renamed to:");
            System.out.println("\t" + getFilePath() + ".corrupted");

            try {
                File corruptedFile = new File(getFilePath());
                corruptedFile.renameTo(new File(getFilePath() + ".corrupted"));
            } catch (NullPointerException | SecurityException e) {
                throw new FileException();
            }
        }
    }

    /**
     * Saves current task list to file.
     *
     * @param bibo Bibo instance to save task list from.
     * @throws FileException if an error occurs while saving the file.
     */
    protected void saveTaskList(TaskList taskList) throws FileException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(getFilePath()))) {
            writer.write(taskList.toFileString());
        } catch (IOException e) {
            throw new FileException();
        }
    }
}
