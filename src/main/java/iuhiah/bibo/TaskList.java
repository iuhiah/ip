package iuhiah.bibo;

import java.time.LocalDateTime;
import java.util.ArrayList;

import iuhiah.bibo.exception.BiboTaskDescriptionException;
import iuhiah.bibo.exception.BiboTodoListIndexException;
import iuhiah.bibo.task.Deadline;
import iuhiah.bibo.task.Event;
import iuhiah.bibo.task.Task;
import iuhiah.bibo.task.Todo;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private static Command cmd;
    private ArrayList<Task> tasks;

    /**
     * Constructs a task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Sets command to be checked.
     *
     * @param command Command to set.
     */
    protected static void setCommand(Command command) {
        cmd = command;
    }

    /**
     * Gets size of task list.
     *
     * @return Size of task list.
     */
    public int getTaskListSize() {
        return tasks.size();
    }

    /**
     * Adds task to the todo list.
     * Task types: todo, deadline, event.
     *
     * @param cmd Command to add task.
     * @param args Arguments for task description.
     * @return Task added to todo list.
     * @throws BiboTaskDescriptionException If task description format is invalid.
     */
    protected Task addTask(String args) throws BiboTaskDescriptionException {
        Task task = null;
        try {
            String[] parsedDescription = Parser.parseTaskDescription(args);
            LocalDateTime[] dateTime;

            switch (cmd.getCommandType()) {
            case TODO:
                task = new Todo(parsedDescription[0]);
                break;
            case DEADLINE:
                dateTime = Parser.parseTaskDateTime(parsedDescription);
                task = new Deadline(parsedDescription[0], dateTime[0]);
                break;
            case EVENT:
                dateTime = Parser.parseTaskDateTime(parsedDescription);
                task = new Event(parsedDescription[0], dateTime[0], dateTime[1]);
                break;
            default:
                // should not reach here
                break;
            }
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }

        tasks.add(task);
        return task;
    }

    /**
     * Changes task status in the todo list.
     * Options: mark, unmark, delete.
     *
     * @param index Index of task to change status.
     * @return Task with status changed.
     * @throws BiboTodoListIndexException If task index is invalid.
     */
    protected Task changeTaskStatus(String index) throws BiboTodoListIndexException {
        try {
            int taskIndex = Parser.parseTaskIndex(index) - 1;
            Task task = tasks.get(taskIndex);

            switch (cmd.getCommandType()) {
            case MARK:
                task.markAsDone();
                break;
            case UNMARK:
                task.markAsUndone();
                break;
            case DELETE:
                tasks.remove(taskIndex);
                break;
            default:
                // should not reach here
                break;
            }

            return task;
        } catch (IndexOutOfBoundsException e) {
            throw new BiboTodoListIndexException("Task index out of bounds!");
        } catch (BiboTodoListIndexException e) {
            throw e;
        }
    }

    /**
     * Finds tasks matching keyword in todo list.
     *
     * @param keyword Keyword to match.
     * @return Tasks matching keyword.
     */
    protected ArrayList<String> findTasks(String keyword) {
        ArrayList<String> messages = new ArrayList<>();
        int count = 0;

        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                messages.add((count + 1) + ". " + task.toString() + "\n");
                count++;
            }
        }

        if (count == 0) {
            messages.add("No tasks found!");
        }

        return messages;
    }

    /**
     * Converts task list to string for saving to file.
     *
     * @return Task list in string format.
     */
    protected String toFileString() {
        StringBuilder fileData = new StringBuilder();
        for (Task task : tasks) {
            fileData.append(task.toFileString() + "\n");
        }
        return fileData.toString();
    }

    @Override
    public String toString() {
        if (tasks.size() == 0) {
            return "List is empty!";
        }

        StringBuilder message = new StringBuilder();

        for (int i = 0; i < tasks.size(); i++) {
            message.append((i + 1) + ". " + tasks.get(i).toString() + "\n");
        }

        // remove trailing newline
        message.setLength(message.length() - 1);
        return message.toString();
    }
}
