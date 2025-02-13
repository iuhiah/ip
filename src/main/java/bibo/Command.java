package bibo;

import java.util.ArrayList;

import bibo.exceptions.BiboException;
import bibo.exceptions.ListIndexException;
import bibo.exceptions.NoteFormatException;
import bibo.exceptions.TaskFormatException;
import bibo.exceptions.UnknownCommandException;
import bibo.notes.Note;
import bibo.notes.Notes;
import bibo.task.Task;
import bibo.task.TaskList;

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
            throw new UnknownCommandException();
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
            protected void execute(String args, TaskList taskList, Notes notes) {
                messages.add("Bye. Hope to see you again soon!");
                ui.close();
            }
        },
        LIST {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) {
                messages.add(taskList.toString());

                if (taskList.getTaskListSize() != 0) {
                    messages.add(0, "Here are the tasks in your list:");
                }
            }
        },
        TODO {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) throws TaskFormatException {
                Task task = taskList.addTask(this, args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(taskList);
            }
        },
        DEADLINE {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) throws TaskFormatException {
                Task task = taskList.addTask(this, args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(taskList);
            }
        },
        EVENT {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) throws TaskFormatException {
                Task task = taskList.addTask(this, args);
                messages.add("Got it. I've added this task:\n" + task);
                addTaskListSize(taskList);
            }
        },
        MARK {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) throws ListIndexException {
                Task task = taskList.changeTaskStatus(this, args);
                messages.add("Nice! I've marked this task as done:\n" + task);
            }
        },
        UNMARK {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) throws ListIndexException {
                Task task = taskList.changeTaskStatus(this, args);
                messages.add("Nice! I've marked this task as undone:\n" + task);
            }
        },
        DELETETASK {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) throws ListIndexException {
                Task task = taskList.changeTaskStatus(this, args);
                messages.add("Noted. I've removed this task:\n" + task);
                addTaskListSize(taskList);
            }
        },
        FIND {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) {
                messages = taskList.findTasks(args);

                if (messages.isEmpty()) {
                    messages.add("No matching tasks found.");
                } else {
                    messages.add(0, "Here are the matching tasks in your list:");
                }
            }
        },
        NOTE {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) throws NoteFormatException {
                Note note = notes.add(args);
                messages.add("Got it. I've added this note:\n" + note);
            }
        },
        DELETENOTE {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) throws ListIndexException {
                notes.delete(args);
                messages.add("Noted. I've removed this note:\n" + args);
            }
        },
        NOTES {
            @Override
            protected void execute(String args, TaskList taskList, Notes notes) {
                messages.add(notes.toString());

                if (notes.getNotesSize() != 0) {
                    messages.add(0, "Here are the notes in your list:");
                }
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
        protected void execute(String args, TaskList taskList, Notes notes) throws BiboException {}

        protected void addTaskListSize(TaskList taskList) {
            int size = taskList.getTaskListSize();

            messages.add("Now you have " + size + " task"
                    + (size == 1 ? "" : "s") + " in the list.");
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

    protected String getResponse(String args, TaskList taskList, Notes notes) {
        try {
            cmd.execute(args, taskList, notes);
            storage.saveTaskList(taskList);
        } catch (BiboException e) {
            return e.getMessage();
        }

        return cmd.getResponse();
    }
}
