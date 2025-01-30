package bibo;

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

import bibo.Parser.ValidCommands;
import bibo.exception.BiboException;
import bibo.exception.BiboTodoListFileException;
import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;
import bibo.exception.BiboUnknownCommandException;
import bibo.task.Task;
import bibo.task.Todo;
import bibo.task.Deadline;
import bibo.task.Event;

public class Bibo {
    private Scanner scanner;
    protected ArrayList<Task> todoList;
    private FileHandler fileHandler;

    private void speak(String message) {
        System.out.println("\n---------- Bibo says: ----------");
        System.out.println(message);
    }
    
    /**
     * Reads user input and determines if input is redirected.
     * If input is redirected, echo input to console for logging purposes.
     * 
     * @return User input.
     * @throws IOException if an error occurs while reading input.
     */
    private String getCommand() throws IOException {
        System.out.println("\n----------- You say: -----------");
        String input = this.scanner.nextLine();

        // for logging purposes
        // if input is redirected
        if (System.console() == null) {
            System.out.println(input);
        }
            
        return input;
    }

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
            String input = this.getCommand();

            String strCmd = input.split(" ")[0];
            Parser.ValidCommands cmd = Parser.checkValidCommand(strCmd);
            String args = input.replace(strCmd, "").trim();

            switch (cmd) {
            case BYE:
                this.speak("Bye. Hope to see you again soon!");
                return;
            case LIST:
                this.showList();
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
                Task task = this.addTask(cmd, args);
                if (task != null) {
                    this.addedTaskSpeak(task);
                    this.fileHandler.saveTodoList(this);
                }
                break;
            case MARK:
            case UNMARK:
            case DELETE:
                this.changeTaskStatus(cmd, args);
                this.fileHandler.saveTodoList(this);
                break;
            }
        } catch (BiboException e) {
            this.speak(e.toString());
        } catch (IOException e) {
            this.speak("Error reading input.");
        }
        this.run();
    }

    private void showList() {
        StringBuilder message = new StringBuilder();
        message.append("Here is your todo list:");

        for (int i = 0; i < this.todoList.size(); i++) {
            message.append(
                "\n" +(i + 1) + ". "
                + this.todoList.get(i).toString()
            );
        }
        this.speak(message.toString());
    }

    protected Task addTaskFromFile(char taskType, String input, boolean done) throws BiboTaskDescriptionException, BiboUnknownCommandException {
        try {
            String taskDescription = Parser.parseTaskDescription(taskType, input);

            ValidCommands cmd;
            switch (taskType) {
            case 'T':
                cmd = ValidCommands.TODO;
                break;
            case 'D':
                cmd = ValidCommands.DEADLINE;
                break;
            case 'E':
                cmd = ValidCommands.EVENT;
                break;
            default:
                throw new BiboUnknownCommandException();
            }

            Task task = this.addTask(cmd, taskDescription);
            if (done) {
                task.markAsDone();
            }
            return task;
        } catch (BiboTaskDescriptionException e) {
            throw e; 
        } catch (BiboUnknownCommandException e) {
            throw e;
        }
    }

    // suppress switch case warning as input has already been validated
    @SuppressWarnings("incomplete-switch")
    private Task addTask(Parser.ValidCommands cmd, String args) throws BiboTaskDescriptionException {
        Task task = null;
        try {
            switch (cmd) {
            case TODO:
                task = this.addTodo(args);
                break;
            case DEADLINE:
                task = this.addDeadline(args);
                break;
            case EVENT:
                task = this.addEvent(args);
                break;
            }
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
        return task;
    }

    private void addedTaskSpeak(Task task) {
        int size = this.todoList.size();
        
        StringBuilder message = new StringBuilder();
        message.append("Got it. I've added this task:\n");
        message.append(task.toString());
        this.taskSpeak(message, size);
    }

    private void taskSpeak(StringBuilder message, int size) {
        message.append("\nNow you have " + size);
        message.append(size == 1 ? " task" : " tasks");
        message.append(" in the list.");
        this.speak(message.toString());
    }

    private Task addTodo(String description) throws BiboTaskDescriptionException {
        try {
            String parsedDescription =
                Parser.parseTaskDescription(Parser.ValidCommands.TODO, description)[0];
            Task task = new Todo(parsedDescription);
            this.todoList.add(task);
            return task;
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    private Task addDeadline(String description) throws BiboTaskDescriptionException {
        try {
            String[] parsedDescription =
                Parser.parseTaskDescription(Parser.ValidCommands.DEADLINE, description);
            Task task = new Deadline(parsedDescription[0],
                                     parsedDescription[1]);
            this.todoList.add(task);
            return task;
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    private Task addEvent(String description) throws BiboTaskDescriptionException {
        try {
            String[] parsedDescription =
                Parser.parseTaskDescription(Parser.ValidCommands.EVENT, description);
            Task task = new Event(parsedDescription[0],
                                  parsedDescription[1],
                                  parsedDescription[2]);
            this.todoList.add(task);
            return task;
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    // suppress switch case warning as input has already been validated
    @SuppressWarnings("incomplete-switch")
    private void changeTaskStatus(Parser.ValidCommands cmd, String args) throws BiboTodoListIndexException, BiboTodoListFileException {
        try {
            int index = Parser.parseTaskIndex(args);
            Task task = this.todoList.get(index - 1);
            
            switch (cmd) {
            case MARK:
                this.markTask(task);
                break;
            case UNMARK:
                this.unmarkTask(task);
                break;
            case DELETE:
                this.deleteTask(task);
                break;
            }
            this.fileHandler.saveTodoList(this);
        } catch (IndexOutOfBoundsException e) {
            throw new BiboTodoListIndexException("Task index out of range!");
        } catch (BiboTodoListFileException e) {
            throw e;
        }
    }

    private void markTask(Task task) { 
        task.markAsDone();
        
        StringBuilder message = new StringBuilder();
        message.append("Alright! I've marked this task as done:\n");
        message.append(task.toString());
        this.speak(message.toString());
    }

    private void unmarkTask(Task task) {
        task.markAsUndone();
        
        StringBuilder message = new StringBuilder();
        message.append("Alright! I've marked this task as undone:\n");
        message.append(task.toString());
        this.speak(message.toString());
    }

    private void deleteTask(Task task) {
        this.todoList.remove(task);
        
        StringBuilder message = new StringBuilder();
        message.append("Alright! I've deleted this task:\n");
        message.append(task.toString());
        this.taskSpeak(message, this.todoList.size());
    }

    /**
     * Update todo list data by reading file.
     * 
     * @throws BiboException if an error occurs while updating todo list.
     */
    public Bibo() throws BiboException {
        this.todoList = new ArrayList<Task>();
        this.scanner = new Scanner(System.in);
        this.fileHandler = new FileHandler();
    }

    /**
     * Initialise Bibo and start conversation.
     * 
     * @param args Command line arguments.
     * @throws BiboException if an error occurs while processing commands.
     */
    public static void main(String[] args) throws BiboException {
        Bibo bibo = new Bibo();
        try {
            bibo.fileHandler.loadTodoList(bibo);
        } catch (BiboTodoListFileException e) {
            throw e;
        }
        bibo.speak("Hello! I'm Bibo.\nWhat can I do for you?");
        bibo.run();
    }
}