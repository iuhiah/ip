package bibo;

import java.time.LocalDateTime;
import java.util.ArrayList;

import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;
import bibo.exception.BiboUnknownCommandException;
import bibo.task.Deadline;
import bibo.task.Event;
import bibo.task.Task;
import bibo.task.Todo;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
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
     * Constructs an update message after performing a task action.
     *
     * @param cmd
     * @param task
     * @return Update message.
     */
    protected String updateTaskMessage(Parser.Commands cmd, Task task) {
        StringBuilder message = new StringBuilder();

        switch (cmd) {
        case TODO:
        case DEADLINE:
        case EVENT:
            message.append("Got it. I've added this task:\n");
            break;
        case MARK:
            message.append("Alright! I've marked this task as done:\n");
            break;
        case UNMARK:
            message.append("Alright! I've marked this task as undone:\n");
            break;
        case DELETE:
            message.append("Alright! I've deleted this task:\n");
            break;
        default:
            // should not reach here
            break;
        }

        message.append(task.toString());
        if (cmd != Parser.Commands.MARK && cmd != Parser.Commands.UNMARK) {
            message.append("\nNow you have " + tasks.size() + " tasks in the list.");
        }

        return message.toString();
    }

    /**
     * Adds task to the todo list from file data.
     *
     * @param taskData Task data read from file.
     * @return Task added to todo list.
     * @throws BiboTaskDescriptionException if task description is invalid.
     * @throws BiboUnknownCommandException if task type is unknown.
     */
    protected Task addTaskFromFile(String taskData) throws BiboTaskDescriptionException, BiboUnknownCommandException {
        try {
            Parser.Commands cmd = Parser.getTaskType(taskData.charAt(0));
            String taskDescription = taskData.split("\\|", 2)[1].trim();
            Task task = addTask(cmd, taskDescription);

            if (taskData.charAt(1) == '1') {
                task.markAsDone();
            }
            return task;
        } catch (BiboTaskDescriptionException e) {
            throw e;
        } catch (BiboUnknownCommandException e) {
            throw e;
        }
    }

    protected Task addTask(Parser.Commands cmd, String args) throws BiboTaskDescriptionException {
        Task task = null;
        try {
            switch (cmd) {
            case TODO:
                task = addTodo(args);
                break;
            case DEADLINE:
                task = addDeadline(args);
                break;
            case EVENT:
                task = addEvent(args);
                break;
            default:
                // should not reach here
                break;
            }
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
        return task;
    }

    private Task addTodo(String description) throws BiboTaskDescriptionException {
        try {
            String parsedDescription =
                Parser.parseTaskDescription(Parser.Commands.TODO, description)[0];
            Task task = new Todo(parsedDescription);
            tasks.add(task);
            return task;
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    private Task addDeadline(String description) throws BiboTaskDescriptionException {
        try {
            String[] parsedDescription =
                Parser.parseTaskDescription(Parser.Commands.DEADLINE, description);
            LocalDateTime[] dateTime =
                Parser.parseTaskDateTime(parsedDescription);
            Task task = new Deadline(parsedDescription[0], dateTime[0]);
            tasks.add(task);
            return task;
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    private Task addEvent(String description) throws BiboTaskDescriptionException {
        try {
            String[] parsedDescription =
                Parser.parseTaskDescription(Parser.Commands.EVENT, description);
            LocalDateTime[] dateTime =
                Parser.parseTaskDateTime(parsedDescription);
            Task task = new Event(parsedDescription[0],
                                  dateTime[0],
                                  dateTime[1]);
            tasks.add(task);
            return task;
        } catch (BiboTaskDescriptionException e) {
            throw e;
        }
    }

    protected Task changeTaskStatus(Parser.Commands cmd, String args) throws BiboTodoListIndexException {
        try {
            int index = Parser.parseTaskIndex(args);
            Task task = tasks.get(index - 1);

            switch (cmd) {
            case MARK:
                markTask(task);
                break;
            case UNMARK:
                unmarkTask(task);
                break;
            case DELETE:
                deleteTask(task);
                break;
            default:
                // should not reach here
                break;
            }

            return task;
        } catch (IndexOutOfBoundsException e) {
            throw new BiboTodoListIndexException("Task index out of range!");
        }
    }

    private void markTask(Task task) {
        task.markAsDone();
    }

    private void unmarkTask(Task task) {
        task.markAsUndone();
    }

    private void deleteTask(Task task) {
        tasks.remove(task);
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
        StringBuilder message = new StringBuilder();
        message.append("Here is your todo list:");

        for (int i = 0; i < tasks.size(); i++) {
            message.append(
                "\n" + (i + 1) + ". "
                + tasks.get(i).toString()
            );
        }
        return message.toString();
    }
}
