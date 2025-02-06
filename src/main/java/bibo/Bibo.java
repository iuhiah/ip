package bibo;

import java.io.IOException;

import bibo.exceptions.BiboException;
import bibo.exceptions.FileException;
import bibo.task.Task;
import bibo.utils.FileParser;
import bibo.utils.InputParser;

/**
 * Represents a personal assistant that helps manage tasks.
 */
public class Bibo {
    protected TaskList taskList;
    private Storage storage;
    private Ui ui;
    private Command cmd;

    /**
     * Initialises new Bibo instance and updates task list from storage.
     *
     * @throws BiboException if an error occurs while updating task list.
     */
    public Bibo() {
        this.ui = new Ui();
        this.taskList = new TaskList();
        this.storage = new Storage();

        this.cmd = new Command(ui, storage);
    }

    /**
     * Loads task list from storage.
     *
     * @param bibo Bibo instance to load task list into.
     * @throws BiboException if an error occurs while loading task list.
     */
    private void loadTaskList() {
        String[] allTaskData = null;

        try {
            allTaskData = storage.loadTaskList();
        } catch (FileException e) {
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < allTaskData.length; i++) {
            String taskData = allTaskData[i];

            try {
                String[] parsedTaskData = FileParser.parseTaskData(taskData);
                cmd.setCommandType(parsedTaskData[0]);
                Task task = taskList.addTask(cmd.getCommandType(), parsedTaskData[1]);

                if (parsedTaskData[2].equals("true")) {
                    task.markAsDone();
                }
            } catch (BiboException e) {
                System.out.println(e.getMessage());
                System.out.println("Skipping task at line " + (i + 1) + ".");
            }
        }

        try {
            storage.checkCorruptedData(taskList.getTaskListSize(), allTaskData.length);
            storage.saveTaskList(taskList);
            System.out.println("File setup complete.");
        } catch (FileException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Task list loaded successfully.");
    }

    /**
     * Executes main loop of Bibo.
     * Continuously reads user input and performs actions until user exits the application.
     * Format of input is expected to be "&lt;command&gt; &lt;arguments&gt;".
     *
     * @throws BiboException if an error occurs while processing commands.
     */
    private void run() {
        try {
            String input = ui.getInput();
            String[] args = InputParser.parseInput(input);
            cmd.setCommandType(args[0]);
            cmd.run(args[1], taskList);
        } catch (BiboException e) {
            ui.speak(e.getMessage());
        } catch (IOException e) {
            ui.speak("Error reading input.");
        }

        if (ui.isRunning) {
            run();
        }
    }

    /**
     * Gets response from Bibo based on single user input.
     *
     * @param input
     * @return Response from Bibo.
     */
    public String getResponse(String input) {
        String response;

        try {
            String[] args = InputParser.parseInput(input);
            cmd.setCommandType(args[0]);
            response = cmd.getResponse(args[1], taskList);
        } catch (BiboException e) {
            response = e.getMessage();
        }

        return response;
    }

    /**
     * Initialises program and starts Bibo.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Bibo bibo = new Bibo();
        bibo.loadTaskList();
        bibo.ui.open();
        bibo.run();
    }
}
