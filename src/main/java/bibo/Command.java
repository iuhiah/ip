package bibo;

import java.util.ArrayList;

import bibo.exception.BiboException;
import bibo.exception.BiboTaskDescriptionException;
import bibo.exception.BiboTodoListIndexException;
import bibo.exception.BiboUnknownCommandException;
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

    protected void setCommandType(String command) throws BiboUnknownCommandException {
        try {
            cmd = CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BiboUnknownCommandException("I'm sorry, but I don't know what that means.");
        }
    }

    protected CommandType getCommandType() {
        return cmd;
    }

    protected enum CommandType {
        BYE {
            @Override
            protected void execute(String args, TaskList todoList) {
                ui.close();
            }
        },
        LIST {
            @Override
            protected void execute(String args, TaskList todoList) {
                ui.speak();
                ui.join(todoList.toString());
            }
        },
        TODO {
            @Override
            protected void execute(String args, TaskList todoList) throws BiboTaskDescriptionException {
                Task task = todoList.addTask(args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(todoList);
            }
        },
        DEADLINE {
            @Override
            protected void execute(String args, TaskList todoList) throws BiboTaskDescriptionException {
                Task task = todoList.addTask(args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(todoList);
            }
        },
        EVENT {
            @Override
            protected void execute(String args, TaskList todoList) throws BiboTaskDescriptionException {
                Task task = todoList.addTask(args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(todoList);
            }
        },
        MARK {
            @Override
            protected void execute(String args, TaskList todoList) throws BiboTodoListIndexException {
                Task task = todoList.changeTaskStatus(args);
                messages.add("Nice! I've marked this task as done:\n" + task);
            }
        },
        UNMARK {
            @Override
            protected void execute(String args, TaskList todoList) throws BiboTodoListIndexException {
                Task task = todoList.changeTaskStatus(args);
                messages.add("Nice! I've marked this task as undone:\n" + task);
            }
        },
        DELETE {
            @Override
            protected void execute(String args, TaskList todoList) throws BiboTodoListIndexException {
                Task task = todoList.changeTaskStatus(args);
                messages.add("Noted. I've removed this task:\n" + task);
                addTaskListSize(todoList);
            }
        },
        FIND {
            @Override
            protected void execute(String args, TaskList todoList) {
                messages = todoList.findTasks(args);
            }
        };

        private static ArrayList<String> messages = new ArrayList<String>();

        /**
         * Executes command.
         *
         * @param args Arguments for command.
         * @param todoList Task list to execute command on.
         * @throws BiboException If an error occurs during execution.
         */
        protected void execute(String args, TaskList todoList) throws BiboException {}

        protected void addTaskListSize(TaskList todoList) {
            messages.add("Now you have " + todoList.getTaskListSize() + " tasks in the list.");
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
    }

    protected void run(String args, TaskList todoList) {
        try {
            cmd.execute(args, todoList);
            cmd.printMessages();
            storage.saveTodoList(todoList);
        } catch (BiboException e) {
            ui.speak(e.getMessage());
        }
    }
}
