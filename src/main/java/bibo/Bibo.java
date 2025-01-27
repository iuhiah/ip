package bibo;

import java.util.Scanner;
import java.util.stream.Stream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import bibo.Parser.ValidCommands;
import bibo.exception.BiboException;
import bibo.exception.BiboTodoListFileException;
import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;
import bibo.task.Task;
import bibo.task.Todo;
import bibo.task.Deadline;
import bibo.task.Event;

public class Bibo {
    private Scanner scanner;
    private ArrayList<Task> todoList;

    private void speak(String message) {
        System.out.println("\n---------- Bibo says: ----------");
        System.out.println(message);
    }
    
    // TODO: Move all file operations to separate class

    /*
     * Checks if todo list file exists. If not, creates one.
     * 
     * @throws BiboTodoListFileException if an error occurs while creating the file.
     */
    private void checkTodoListFile() throws BiboTodoListFileException {
        try {
            if (!Files.exists(Paths.get("data"))) {
                System.out.println("Saved data not found. Creating new file to store data...");
                Files.createDirectory(Paths.get("data"));
                Files.createFile(Paths.get("data/todo.txt"));
            }
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error creating todo list file.");
        }
    }
    
    /**
     * Updates Bibo's todo list with data from file.
     * 
     * @throws BiboTodoListFileException if file data is corrupted.
     */
    private void loadTodoList() throws BiboTodoListFileException {
        this.checkTodoListFile();
        this.todoList = new ArrayList<>();
        
        try (Stream<String> fileData = Files.lines(Paths.get("data/todo.txt"))) {
            fileData.forEach(taskData -> {
                try {
                    char taskType = taskData.charAt(1);
                    String taskDescription = taskData.substring(7);

                    switch (taskType) {
                    case 'T':
                        this.addTask(ValidCommands.TODO, taskDescription);
                        break;
                    case 'D':
                        this.addTask(ValidCommands.DEADLINE, taskDescription);
                        break;
                    case 'E':
                        this.addTask(ValidCommands.EVENT, taskDescription);
                        break;
                    default:
                        throw new BiboTodoListFileException("Invalid task type in todo list file.");
                    }
                } catch (BiboTaskDescriptionException e) {
                    try {
                        throw new BiboTodoListFileException("Error parsing task data from todo list file.");
                    } catch (BiboTodoListFileException e1) {
                        this.speak(e1.getMessage());
                    }
                } catch (BiboTodoListFileException e) {
                    this.speak(e.getMessage());
                }
            });
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error reading from todo list file.");
        }
    }

    /**
     * Updates todo list file by replacing line at index with new task data. Rest of the file remains unchanged.
     * Adds to EOF if index is -1.
     * 
     * @param index Index of task to update.
     * @param task New task data.
     */
    private void updateTodoListFile(int index, Task task) throws BiboTodoListFileException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("data/todo.txt"))) {
            if (index == -1) {
                writer.write(this.todoList.get(this.todoList.size() - 1).toString());
            } else {
                for (int i = 0; i < this.todoList.size(); i++) {
                    if (i == index) {
                        writer.write(task.toString());
                    } else {
                        writer.write(this.todoList.get(i).toString());
                    }
                    writer.newLine();
                }
            }

            writer.flush();
        } catch (IOException e) {
            throw new BiboTodoListFileException("Error updating todo list file.");
        }
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
        try {
            // if input is redirected
            if (System.in.available() == 0) {
                // echo input to console
                System.out.println(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                this.addTask(cmd, args);
                break;
            case MARK:
            case UNMARK:
            case DELETE:
                this.changeTaskStatus(cmd, args);
                break;
            }
        } catch (Exception e) {
            this.speak(e.toString());
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

    // TODO: All task methods to return Task object

    private void addTask(Parser.ValidCommands cmd, String args) throws BiboTaskDescriptionException, BiboTodoListFileException {
        try {
            switch (cmd) {
            case TODO:
                this.addTodo(args);
                break;
            case DEADLINE:
                this.addDeadline(args);
                break;
            case EVENT:
                this.addEvent(args);
                break;
            }
            this.addedTaskSpeak();
            this.updateTodoListFile(-1, null);
        } catch (BiboTaskDescriptionException e) {
            this.speak(e.toString());
        } catch (BiboTodoListFileException e) {
            this.speak(e.toString());
        }
    }

    private void addedTaskSpeak() {
        int size = this.todoList.size();
        
        StringBuilder message = new StringBuilder();
        message.append("Got it. I've added this task:\n");
        message.append(todoList.get(size-1).toString());
        this.taskSpeak(message, size);
    }

    private void taskSpeak(StringBuilder message, int size) {
        message.append("\nNow you have " + size);
        message.append(size == 1 ? " task" : " tasks");
        message.append(" in the list.");
        this.speak(message.toString());
    }

    private void addTodo(String description) throws BiboTaskDescriptionException {
        try {
            String parsedDescription =
                Parser.parseTaskDescription(Parser.ValidCommands.TODO, description)[0];
            this.todoList.add(new Todo(parsedDescription));
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    private void addDeadline(String description) throws BiboTaskDescriptionException {
        try {
            String[] parsedDescription =
                Parser.parseTaskDescription(Parser.ValidCommands.DEADLINE,description);
            this.todoList.add(
                new Deadline(parsedDescription[0],
                             parsedDescription[1])
            );
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    private void addEvent(String description) throws BiboTaskDescriptionException {
        try {
            String[] parsedDescription =
                Parser.parseTaskDescription(Parser.ValidCommands.EVENT, description);
            this.todoList.add(
                new Event(parsedDescription[0],
                          parsedDescription[1],
                          parsedDescription[2])
            );
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

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
            this.updateTodoListFile(index - 1, task);
        } catch (BiboTodoListIndexException e) {
            this.speak(e.toString());
        } catch (BiboTodoListFileException e) {
            this.speak(e.toString());
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
        try {
            this.loadTodoList();
        } catch (BiboTodoListFileException e) {
            throw e;
        }

        this.scanner = new Scanner(System.in);
    }

    /**
     * Initialise Bibo and start conversation.
     * 
     * @param args Command line arguments.
     * @throws BiboException if an error occurs while processing commands.
     */
    public static void main(String[] args) throws BiboException {
        Bibo bibo = new Bibo();
        bibo.speak("Hello! I'm Bibo.\nWhat can I do for you?");
        bibo.run();
    }
}