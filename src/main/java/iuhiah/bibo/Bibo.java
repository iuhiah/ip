package iuhiah.bibo;

import java.io.IOException;

import iuhiah.bibo.exception.BiboException;
import iuhiah.bibo.exception.BiboTodoListFileException;
import iuhiah.bibo.task.Task;

/**
 * Represents a personal assistant that helps manage tasks.
 */
public class Bibo {
    protected TaskList todoList;
    private Storage storage;
    private Ui ui;
    private Command cmd;

    /**
     * Initialises new Bibo instance and updates todo list from storage.
     *
     * @throws BiboException if an error occurs while updating todo list.
     */
    public Bibo() throws BiboException {
        this.ui = new Ui();
        this.todoList = new TaskList();
        this.storage = new Storage();

        this.cmd = new Command(ui, storage);
        Parser.setCommand(cmd);
        TaskList.setCommand(cmd);
    }

    /**
     * Loads todo list from storage.
     *
     * @param bibo Bibo instance to load todo list into.
     * @throws BiboException if an error occurs while loading todo list.
     */
    private void loadTodoList() throws BiboException {
        String[] allTaskData = null;

        try {
            if (storage.hasSavedData()) {
                allTaskData = storage.loadTodoList();

                if (allTaskData == null) {
                    return;
                }

                for (String taskData : allTaskData) {
                    try {
                        // get index of task status marker (first whitespace index - 1)
                        int markerIndex = taskData.indexOf(' ');
                        String parsedTaskData =
                            taskData.substring(0, markerIndex) + taskData.substring(markerIndex + 1);

                        Task task = todoList.addTask(parsedTaskData);

                        if (taskData.charAt(markerIndex) == '1') {
                            task.markAsDone();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                storage.checkCorruptedData(todoList.getTaskListSize(), allTaskData.length);
                storage.saveTodoList(todoList);
                System.out.println("File setup complete.");
            }

        } catch (BiboTodoListFileException e) {
            System.out.println(e.getMessage());
        }
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
            String args = Parser.parseInput(input);
            cmd.run(args, todoList);
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
     * Initialises program and starts Bibo.
     *
     * @param args Command line arguments.
     * @throws BiboException if an error occurs while processing commands.
     */
    public static void main(String[] args) throws BiboException {
        Bibo bibo = new Bibo();
        bibo.loadTodoList();
        bibo.ui.open();
        bibo.run();
    }
}
