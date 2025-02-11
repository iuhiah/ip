package bibo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import bibo.exceptions.TaskFormatException;
import bibo.exceptions.TaskListIndexException;
import bibo.exceptions.UnknownCommandException;
import bibo.task.Deadline;
import bibo.task.Event;
import bibo.task.Task;
import bibo.task.Todo;
import bibo.utils.DateTimeUtil;
import bibo.utils.InputParser;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs a task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        assert(tasks != null);
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
     * Adds task to the task list.
     * Task types: todo, deadline, event.
     *
     * @param cmd Command to add task.
     * @param args Arguments for task description.
     * @return Task added to task list.
     * @throws TaskFormatException If task description format is invalid.
     */
    protected Task addTask(Command.CommandType cmd, String args) throws TaskFormatException {
        Task task = null;
        try {
            String[] parsedDescription = InputParser.parseTaskDescription(cmd, args);
            String[] dateTime = Arrays.copyOfRange(parsedDescription, 1, parsedDescription.length);
            LocalDateTime[] parsedDateTime;

            switch (cmd) {
            case TODO:
                task = new Todo(parsedDescription[0]);
                break;
            case DEADLINE:
                parsedDateTime = DateTimeUtil.parseDateTime(dateTime);
                task = new Deadline(parsedDescription[0], parsedDateTime);
                break;
            case EVENT:
                parsedDateTime = DateTimeUtil.parseDateTime(dateTime);
                task = new Event(parsedDescription[0], parsedDateTime);
                break;
            default:
                // should not reach here
                break;
            }
        } catch (UnknownCommandException e) {
            throw new TaskFormatException(
                TaskFormatException.ErrorType.UNKNOWN_TASK_TYPE.toString()
            );
        } catch (TaskFormatException e) {
            throw e;
        }

        tasks.add(task);
        return task;
    }

    /**
     * Changes task status in the task list.
     * Options: mark, unmark, delete.
     *
     * @param index Index of task to change status.
     * @return Task with status changed.
     * @throws TaskListIndexException If task index is invalid.
     */
    protected Task changeTaskStatus(Command.CommandType cmd, String index) throws TaskListIndexException {
        try {
            int taskIndex = InputParser.parseTaskIndex(index) - 1;
            Task task = tasks.get(taskIndex);

            switch (cmd) {
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
            throw new TaskListIndexException("Task index out of bounds!");
        } catch (TaskListIndexException e) {
            throw e;
        }
    }

    /**
     * Finds tasks matching keyword in task list.
     *
     * @param keyword Keyword to match.
     * @return Tasks matching keyword.
     */
    protected ArrayList<String> findTasks(String keyword) {
        ArrayList<String> messages = tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .map(task -> tasks.indexOf(task) + 1 + ". " + task.toString())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (messages.size() == 0) {
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
        return tasks.stream()
                .map(Task::toFileString)
                .collect(Collectors.joining(null));
    }

    @Override
    public String toString() {
        if (tasks.size() == 0) {
            return "List is empty!";
        }

        return tasks.stream()
                .map(task -> tasks.indexOf(task) + 1 + ". " + task.toString())
                .collect(Collectors.joining("\n"));
    }
}
