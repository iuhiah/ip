package bibo;

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

import bibo.exception.BiboException;
import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;
import bibo.task.Task;
import bibo.task.Todo;
import bibo.task.Deadline;
import bibo.task.Event;

public class Bibo {
    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Task> todoList = new ArrayList<Task>();

    private void speak(String message) {
        System.out.println("\n---------- Bibo says: ----------");
        System.out.println(message);
    }

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
                this.addTodo(args);
                break;
            case DEADLINE:
                this.addDeadline(args);
                break;
            case EVENT:
                this.addEvent(args);
                break;
            case MARK:
                this.markTask(args);
                break;
            case UNMARK:
                this.unmarkTask(args);
                break;
            case DELETE:
                this.deleteTask(args);
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

        String taskFmt;
        for (int i = 0; i < this.todoList.size(); i++) {
            taskFmt = (i + 1) + ". " + this.todoList.get(i).toString();
            message.append("\n" + taskFmt);
        }
        this.speak(message.toString());
    }

    private void addTodo(String description) throws BiboTaskDescriptionException {
        try {
            String parsedDescription = Parser.parseTaskDescription(Parser.ValidCommands.TODO, description)[0];
            this.todoList.add(new Todo(parsedDescription));
            this.addedTaskSpeak();
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    private void addDeadline(String description) throws BiboTaskDescriptionException {
        try {
            String[] parsedDescription = Parser.parseTaskDescription(Parser.ValidCommands.DEADLINE, description);
            this.todoList.add(new Deadline(parsedDescription[0], parsedDescription[1]));
            this.addedTaskSpeak();
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    private void addEvent(String description) throws BiboTaskDescriptionException {
        try {
            String[] parsedDescription = Parser.parseTaskDescription(Parser.ValidCommands.EVENT, description);
            this.todoList.add(new Event(parsedDescription[0], parsedDescription[1], parsedDescription[2]));
            this.addedTaskSpeak();
        } catch (BiboTaskDescriptionException e) {
            throw e;
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

    private void markTask(String strIdx) throws BiboTodoListIndexException {        
        try {
            int index = Parser.parseTaskIndex(strIdx);

            Task task = this.todoList.get(index - 1);
            task.markAsDone();
            
            StringBuilder message = new StringBuilder();
            message.append("Alright! I've marked this task as done:\n");
            message.append(task.toString());

            this.speak(message.toString());
        } catch (BiboTodoListIndexException e) {
            throw e; 
        } catch (IndexOutOfBoundsException e) {
            throw new BiboTodoListIndexException("Index out of bounds.");
        }
    }

    private void unmarkTask(String strIdx) throws BiboTodoListIndexException {
        try {
            int index = Parser.parseTaskIndex(strIdx);

            Task task = this.todoList.get(index - 1);
            task.markAsUndone();
            
            StringBuilder message = new StringBuilder();
            message.append("Alright! I've unmarked this task:\n");
            message.append(task.toString());

            this.speak(message.toString());
        } catch (BiboTodoListIndexException e) {
            throw e;
        } catch (IndexOutOfBoundsException e) {
            throw new BiboTodoListIndexException("Index out of bounds.");
        }
    }

    private void deleteTask(String strIdx) throws BiboTodoListIndexException {
        try {
            int index = Parser.parseTaskIndex(strIdx);

            Task task = this.todoList.get(index - 1);
            this.todoList.remove(index - 1);
            
            StringBuilder message = new StringBuilder();
            message.append("Alright! I've deleted this task:\n");
            message.append(task.toString());

            this.taskSpeak(message, this.todoList.size());
        } catch (BiboTodoListIndexException e) {
            throw e; 
        } catch (IndexOutOfBoundsException e) {
            throw new BiboTodoListIndexException("Index out of bounds.");
        }
    }

    /**
     * Initalise Bibo and start conversation.
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