package bibo.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import bibo.Command;
import bibo.exceptions.ListIndexException;
import bibo.exceptions.TaskFormatException;
import bibo.exceptions.UnknownCommandException;
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
        assert (tasks != null) : "Task list should not be null";
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
     * @param cmd  Command to add task.
     * @param args Arguments for task description.
     * @return Task added to task list.
     * @throws TaskFormatException If task format is invalid.
     */
    public Task addTask(Command.CommandType cmd, String args) throws TaskFormatException {
        Task task = null;

        try {
            String[] parsedDescription = InputParser.parseTaskDescription(cmd, args);

            switch (cmd) {
            case TODO:
                task = new Todo(parsedDescription[0]);
                break;
            case DEADLINE:
                task = addDeadline(parsedDescription);
                break;
            case EVENT:
                task = addEvent(parsedDescription);
                break;
            default:
                throw new UnknownCommandException();
            }
        } catch (UnknownCommandException e) {
            throw new TaskFormatException(
                    TaskFormatException.ErrorType.UNKNOWN_TASK_TYPE.toString());
        } catch (TaskFormatException e) {
            throw e;
        }

        // check if task with same details already exists
        if (tasks.contains(task)) {
            throw new TaskFormatException(
                    TaskFormatException.ErrorType.DUPLICATE_TASK.toString());
        }

        tasks.add(task);
        return task;
    }

    /**
     * Add deadline to the task list.
     *
     * @param parsedDescription Description of deadline.
     * @return Deadline task.
     * @throws TaskFormatException If deadline format is invalid.
     */
    private Deadline addDeadline(String[] parsedDescription) throws TaskFormatException {
        String[] dateTime = Arrays.copyOfRange(parsedDescription, 1, parsedDescription.length);
        LocalDateTime[] parsedDateTime = DateTimeUtil.parseDateTime(dateTime);

        if (parsedDateTime == null || parsedDateTime.length != 1) {
            throw new TaskFormatException(
                    TaskFormatException.ErrorType.MISSING_ARGUMENT.toString());
        }

        return new Deadline(parsedDescription[0], parsedDateTime);
    }

    /**
     * Add event to the task list.
     *
     * @param parsedDescription Description of event.
     * @return Event task.
     * @throws TaskFormatException If event format is invalid.
     */
    private Event addEvent(String[] parsedDescription) throws TaskFormatException {
        String[] dateTime = Arrays.copyOfRange(parsedDescription, 1, parsedDescription.length);
        LocalDateTime[] parsedDateTime = DateTimeUtil.parseDateTime(dateTime);

        if (parsedDateTime == null || parsedDateTime.length != 2) {
            throw new TaskFormatException(
                    TaskFormatException.ErrorType.MISSING_ARGUMENT.toString());
        }

        if (parsedDateTime[0].isAfter(parsedDateTime[1])) {
            throw new TaskFormatException(
                    TaskFormatException.ErrorType.DATE_TIME_INVALID.toString());
        }

        return new Event(parsedDescription[0], parsedDateTime);
    }

    /**
     * Changes task status in the task list.
     * Options: mark, unmark, delete.
     *
     * @param index Index of task to change status.
     * @return Task with status changed.
     * @throws ListIndexException If task index is invalid.
     */
    public Task changeTaskStatus(Command.CommandType cmd, String index)
            throws ListIndexException, UnknownCommandException {
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
            case DELETETASK:
                tasks.remove(taskIndex);
                break;
            default:
                throw new UnknownCommandException();
            }

            return task;
        } catch (IndexOutOfBoundsException e) {
            throw new ListIndexException(
                    ListIndexException.ErrorType.INVALID_INDEX.toString());
        } catch (ListIndexException e) {
            throw e;
        }
    }

    /**
     * Finds tasks matching keyword in task list.
     *
     * @param keyword Keyword to match.
     * @return Tasks matching keyword.
     */
    public ArrayList<String> findTasks(String keyword) {
        ArrayList<String> messages = tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .map(task -> tasks.indexOf(task) + 1 + ". " + task.toString())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return messages;
    }

    /**
     * Converts task list to string for saving to file.
     *
     * @return Task list in string format.
     */
    public String toFileString() {
        return tasks.stream()
                .map(Task::toFileString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "No tasks found!";
        }

        return tasks.stream()
                .map(task -> tasks.indexOf(task) + 1 + ". " + task.toString())
                .collect(Collectors.joining("\n"));
    }
}
