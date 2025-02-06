package bibo;

import java.util.ArrayList;

import bibo.exceptions.BiboException;
import bibo.exceptions.TaskFormatException;
import bibo.exceptions.TaskListIndexException;
import bibo.exceptions.UnknownCommandException;
import bibo.task.Task;

/**
 * Represents a command.
 */
public class Command {
    private static Ui ui;
    private static Storage storage;
    private static CommandType cmd;

    /**
     * Constructs a command.
     *
     * @param cmd Command type.
     * @param ui Ui object.
     * @param storage Storage object.
     */
    public Command(Ui ui, Storage storage) {
        Command.ui = ui;
        Command.storage = storage;
    }

    protected void setCommandType(String command) throws UnknownCommandException {
        try {
            cmd = CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownCommandException("I'm sorry, but I don't know what that means.");
        }
    }

    protected CommandType getCommandType() {
        return cmd;
    }

    /**
     * Represents list of valid commands.
     */
    public enum CommandType {
        BYE {
            @Override
            protected void execute(String args, TaskList taskList) {
                messages.add("Bye. Hope to see you again soon!");
                ui.close();
            }
        },
        LIST {
            @Override
            protected void execute(String args, TaskList taskList) {
                messages.add(taskList.toString());
            }
        },
        TODO {
            @Override
            protected void execute(String args, TaskList taskList) throws TaskFormatException {
                Task task = taskList.addTask(this, args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(taskList);
            }
        },
        DEADLINE {
            @Override
            protected void execute(String args, TaskList taskList) throws TaskFormatException {
                Task task = taskList.addTask(this, args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(taskList);
            }
        },
        EVENT {
            @Override
            protected void execute(String args, TaskList taskList) throws TaskFormatException {
                Task task = taskList.addTask(this, args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(taskList);
            }
        },
        MARK {
            @Override
            protected void execute(String args, TaskList taskList) throws TaskListIndexException {
                Task task = taskList.changeTaskStatus(this, args);
                messages.add("Nice! I've marked this task as done:\n" + task);
            }
        },
        UNMARK {
            @Override
            protected void execute(String args, TaskList taskList) throws TaskListIndexException {
                Task task = taskList.changeTaskStatus(this, args);
                messages.add("Nice! I've marked this task as undone:\n" + task);
            }
        },
        DELETE {
            @Override
            protected void execute(String args, TaskList taskList) throws TaskListIndexException {
                Task task = taskList.changeTaskStatus(this, args);
                messages.add("Noted. I've removed this task:\n" + task);
                addTaskListSize(taskList);
            }
        },
        FIND {
            @Override
            protected void execute(String args, TaskList taskList) {
                messages = taskList.findTasks(args);
            }
        };

        private static ArrayList<String> messages = new ArrayList<String>();

        /**
         * Executes command.
         *
         * @param args Arguments for command.
         * @param taskList Task list to execute command on.
         * @throws BiboException If an error occurs during execution.
         */
        protected void execute(String args, TaskList taskList) throws BiboException {}

        protected void addTaskListSize(TaskList taskList) {
            int size = taskList.getTaskListSize();

            messages.add("Now you have " + size + " task"
                    + (size == 1 ? "" : "s") + " in the list.");
        }

        /**
         * Prints messages through UI.
         *
         * @param messages Messages to print.
         */
        protected void printMessages() {
            if (messages.isEmpty()) {
                return;
            }

            ui.speak();
            for (String message : messages) {
                ui.join(message);
            }
            messages.clear();
        }

        protected String getResponse() {
            StringBuilder message = new StringBuilder("");
            for (String msg : messages) {
                message.append(msg).append("\n");
            }
            messages.clear();
            return message.toString();
        }
    }

    protected void run(String args, TaskList taskList) {
        try {
            cmd.execute(args, taskList);
            cmd.printMessages();
            storage.saveTaskList(taskList);
        } catch (BiboException e) {
            ui.speak(e.getMessage());
        }
    }

    protected String getResponse(String args, TaskList taskList) {
        try {
            cmd.execute(args, taskList);
            storage.saveTaskList(taskList);
        } catch (BiboException e) {
            return e.getMessage();
        }

        return cmd.getResponse();
    }
}
