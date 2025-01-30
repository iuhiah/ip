package bibo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListFileException;
import bibo.exception.BiboUnknownCommandException;
import bibo.task.Task;

public class FileHandler {
    private String dataDir = "data";
    private String fileName = "todo.txt";

    /*
     * Checks if todo list file exists. If not, creates one.
     * 
     * @throws BiboTodoListFileException if an error occurs while creating the file.
     */
    private  void checkTodoListFile() throws BiboTodoListFileException {
        try {
            if (!Files.exists(Paths.get("data"))) {
                System.out.println("Saved data not found. Creating new file to store data...");
                Files.createDirectory(Paths.get(this.dataDir));
                Files.createFile(Paths.get(this.dataDir + "/" + this.fileName));
            }
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error creating todo list file.");
        }
    }
    
    /**
     * Updates Bibo's todo list with data from file.
     * If file is corrupted, renames file to .corrupted and
     * creates a new file with non-corrupted data.
     * 
     * @param bibo Bibo instance to load todo list into.
     * @throws BiboTodoListFileException if file data is corrupted.
     */
     protected void loadTodoList(Bibo bibo) throws BiboTodoListFileException {
        System.out.println("Loading todo list...");
        checkTodoListFile();
        int totalTasks = 0;
        
        try (BufferedReader fileReader = new BufferedReader(new FileReader(this.dataDir + "/" + this.fileName))) {
            String taskData;
            while ((taskData = fileReader.readLine()) != null) {
                try {
                    bibo.addTaskFromFile(taskData.charAt(1),
                                         taskData.substring(7),
                                         taskData.charAt(4) == 'X');
                } catch (BiboTaskDescriptionException e) {
                    System.out.println("Error parsing task data from file. Skipping task:");
                    System.out.println("\t" + taskData);
                } catch (BiboUnknownCommandException e) {
                    System.out.println("Error parsing task type from file. Skipping task:");
                    System.out.println("\t" + taskData);
                } finally {
                    totalTasks++;
                }
            }
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error reading from todo list file.");
        }

        initialFileUpdate(bibo, totalTasks);
    }

    private void initialFileUpdate(Bibo bibo, int totalTasks) throws BiboTodoListFileException {
        int loadedTasks = bibo.todoList.size();
        System.out.println("Successfully loaded " + loadedTasks + " of " + totalTasks + " tasks.");

        if (loadedTasks < totalTasks) {
            System.out.println("Some data was corrupted, corrupted data file renamed to:");
            System.out.println("\t" + this.dataDir + "/" + this.fileName + ".corrupted");

            try {
                File corruptedFile = new File(this.dataDir + "/" + this.fileName);
                corruptedFile.renameTo(new File(this.dataDir + "/" + this.fileName + ".corrupted"));
            } catch (Exception e) {
                throw new BiboTodoListFileException("Error renaming corrupted file.");
            }

            System.out.println("Non-corrupted data will be saved to original file name.");
        }
        
        try {
            this.saveTodoList(bibo);
        } catch (BiboTodoListFileException e) {
            throw e;
        }
    }

    /**
     * Saves current todo list to file.
     * 
     * @param bibo Bibo instance to save todo list from.
     * @throws BiboTodoListFileException if an error occurs while saving the file.
     */
     protected void saveTodoList(Bibo bibo) throws BiboTodoListFileException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.dataDir + "/" + this.fileName))) {
            for (Task task : bibo.todoList) {
                writer.write(task.toString());
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error saving todo list file.");
        }
    }
}
