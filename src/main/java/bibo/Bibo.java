package bibo;

import java.io.IOException;

import bibo.exception.BiboException;
import bibo.exception.BiboTodoListFileException;
import bibo.task.Task;

/**
 * Represents a personal assistant that helps manage tasks.
 */
public class Bibo {
    private Ui ui;
    private Storage storage;
    protected TaskList todoList;


    /**
     * Executes main loop of Bibo.
     * Continuously reads user input and performs actions until user exits the application.
     * Format of input is expected to be "<command> <arguments>".
     * 
     * Commands supported:
     * - bye: Exits the application.
     * - list: Lists all tasks currently in the todo list.
     * - todo <description>: Adds a new todo task.
     * - deadline <description> /by <date>: Adds a new task with a deadline.
     * - event <description> /from <start> /to <end>: Adds a new event task with a duration.
     * - mark <index>: Marks a task as done.
     * - unmark <index>: Marks a task as undone.
     * - delete <index>: Deletes a task from the todo list.
     * 
     * If an invalid command is entered, an exception is thrown and an error message is displayed.
     * 
     * @throws BiboException if an error occurs while processing commands.
     */
    private void run() {
        try {
            String input = ui.getCommand();

            String strCmd = input.split(" ")[0];
            Parser.Commands cmd = Parser.checkValidCommand(strCmd);
            String args = input.replace(strCmd, "").trim();

            Task task = null;

            switch (cmd) {
            case BYE:
                ui.close();
                return;
            case LIST:
                ui.speak(todoList.toString());
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
                task = todoList.addTask(cmd, args);
                break;
            case MARK:
            case UNMARK:
            case DELETE:
                task = todoList.changeTaskStatus(cmd, args);
                break;
            }

            if (task != null) {
                ui.speak(todoList.updateTaskMessage(cmd, task));
                storage.saveTodoList(this);
            }
        } catch (BiboException e) {
            ui.speak(e.toString());
        } catch (IOException e) {
            ui.speak("Error reading input.");
        }
        
        run();
    }

    /**
     * Updates todo list data by reading from file.
     * 
     * @throws BiboException if an error occurs while updating todo list.
     */
    public Bibo() throws BiboException {
        this.ui = new Ui();
        this.todoList = new TaskList();
        this.storage = new Storage();

        try {
            storage.loadTodoList(this);
        } catch (BiboTodoListFileException e) {
            throw e;
        }

        ui.open();
    }

    /**
     * Initialises new Bibo instance and starts conversation.
     * 
     * @param args Command line arguments.
     * @throws BiboException if an error occurs while processing commands.
     */
    public static void main(String[] args) throws BiboException {
        Bibo bibo = new Bibo();
        bibo.run();
    }
}