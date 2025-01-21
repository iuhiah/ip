import java.util.Scanner;

public class Bibo {
    private Scanner scanner = new Scanner(System.in);
    private Task[] todolist = new Task[100];
    private int todoPointer = 0;

    private String biboSays = "\n---------- Bibo says: ----------";
    private enum Commands {
        BYE, LIST,
        TODO, DEADLINE, EVENT,
        MARK, UNMARK
    }

    private void speak(String message) {
        System.out.println(message);
    }

    private void commands() {
        try {
            this.speak("\n----------- You say: -----------");
            String input = this.scanner.nextLine();
            this.speak(this.biboSays);

            String strCommand = input.split(" ")[0];
            Commands command = this.checkValidCommand(strCommand);
            String args = input.replace(strCommand, "").trim();

            switch (command) {
                case BYE:
                    this.BiboBye();
                    this.scanner.close();
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
            }
        } catch (BiboException e) {
            this.speak(e.toString());
        }
        this.speak("What's next?");
        this.commands();
    }

    private Commands checkValidCommand(String command) throws BiboUnknownCommandException {
        try {
            return Commands.valueOf(command.toUpperCase());
        } catch (Exception e) {
            throw new BiboUnknownCommandException(command);
        }
    }

    private void addTodo(String description) throws BiboTaskDescriptionException {
        if (description.isEmpty()) {
            throw new BiboTaskDescriptionException("Todo");
        }

        this.todolist[todoPointer] = new Todo(description);
        this.todoPointer++;
        this.addedTaskSpeak();
    }

    private void addDeadline(String description) throws BiboTaskDescriptionException {
        try {
            String[] split = description.split(" /by ");
            description = split[0];
            String by = split[1];

            this.todolist[todoPointer] = new Deadline(description, by);
            this.todoPointer++;
            this.addedTaskSpeak();
        } catch (Exception e) {
            throw new BiboTaskDescriptionException("Deadline");
        }
    }

    private void addEvent(String description) throws BiboTaskDescriptionException {
        try {
            String[] split = description.split(" /from | /to ");
            description = split[0];
            String start = split[1];
            String end = split[2];

            this.todolist[todoPointer] = new Event(description, start, end);
            this.todoPointer++;
            this.addedTaskSpeak();
        } catch (Exception e) {
            throw new BiboTaskDescriptionException("Event");
        }
    }

    private void addedTaskSpeak() {
        this.speak("Got it. I've added this task:");
        this.speak(todolist[todoPointer - 1].toString());
        
        if (todoPointer == 1) {
            this.speak("Now you have 1 task in the list.");
        } else {
            this.speak("Now you have " + todoPointer + " tasks in the list.");
        }
    }

    private void markTask(String strIdx) throws BiboTodoListIndexException {
        try {
            int index = Integer.parseInt(strIdx);
            this.todolist[index - 1].markAsDone();
            this.speak("Nice! I've marked this task as done:");
            this.speak(todolist[index - 1].toString());
        } catch (Exception e) {
            throw new BiboTodoListIndexException(strIdx);
        }
    }

    private void unmarkTask(String strIdx) throws BiboTodoListIndexException {
        try {
            int index = Integer.parseInt(strIdx);
            this.todolist[index - 1].markAsUndone();
            this.speak("Alright! I've unmarked this task:");
            this.speak(todolist[index - 1].toString());
        } catch (Exception e) {
            throw new BiboTodoListIndexException(strIdx);
        }
    }

    private void showList() {
        this.speak("Here is your to-do list:");

        for (int i = 0; i < todoPointer; i++) {
            this.speak((i + 1) + ". " + todolist[i]);
        }
    }

    private void BiboGreet() {
        this.speak(this.biboSays);
        this.speak("Hello! I'm Bibo.\nWhat can I do for you?");
    }

    private void BiboBye() {
        this.speak("Bye. Hope to see you again soon!");
    }

    public static void main(String[] args) throws BiboException {
        Bibo bibo = new Bibo();
        bibo.BiboGreet();
        bibo.commands();
    }
}