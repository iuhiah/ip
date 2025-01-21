import java.util.Scanner;

public class Bibo {
    private Scanner input = new Scanner(System.in);
    private Task[] todolist = new Task[100];
    private int todoPointer = 0;

    private String biboSays = "\n---------- Bibo says: ----------";

    private void speak(String message) {
        System.out.println(message);
    }

    private void commands() {
        while (true) {
            this.speak("\n----------- You say: -----------");
            String command = this.input.nextLine();

            this.speak(this.biboSays);

            if (command.equals("bye")) {
                break;
            }
            
            if (command.equals("list")) {
                this.showList();
                continue;
            }

            String keyword = command.split(" ")[0];
            // not index +1 in case mistyped input is only one word long (no spaces)
            String args = command.substring(keyword.length()).trim();
            
            if (keyword.equals("mark")) {
                this.markTask(Integer.parseInt(args));
            }
            else if (keyword.equals("unmark")) {
                this.unmarkTask(Integer.parseInt(args));
            }
            else if (keyword.equals("todo")) {
                this.addTodo(args);
            }
            else if (keyword.equals("deadline")) {
                String[] parts = args.split(" /by ");
                this.addDeadline(parts[0], parts[1]);
            }
            else if (keyword.equals("event")) {
                String[] parts = args.split("( /from | /to )");
                this.addEvent(parts[0], parts[1], parts[2]);
            }
            else {
                this.speak("I'm sorry, I don't understand that command. Try again!");
            }

            this.speak("What's next?");
        }
    }

    private void addTodo(String description) {
        this.todolist[todoPointer] = new Todo(description);
        this.todoPointer++;
        this.addedTaskSpeak();
    }

    private void addDeadline(String description, String by) {
        this.todolist[todoPointer] = new Deadline(description, by);
        this.todoPointer++;
        this.addedTaskSpeak();
    }

    private void addEvent(String description, String start, String end) {
        this.todolist[todoPointer] = new Event(description, start, end);
        this.todoPointer++;
        this.addedTaskSpeak();
    }

    private void addedTaskSpeak() {
        this.speak("Got it. I've added this task:");
        this.speak(todolist[todoPointer - 1].toString());
        this.speak("Now you have " + todoPointer + " tasks in the list.");
    }

    private void markTask(int index) {
        if (index < 1 || index > todoPointer) {
            this.speak("That's not in your to-do list! Try something else.");
            return;
        }

        this.todolist[index - 1].markAsDone();
        this.speak("Nice! I've marked this task as done:");
        this.speak(todolist[index - 1].toString());
    }

    private void unmarkTask(int index) {
        if (index < 1 || index > todoPointer) {
            this.speak("That's not in your to-do list! Try something else.");
            return;
        }

        this.todolist[index - 1].markAsUndone();
        this.speak("Alright! I've unmarked this task:");
        this.speak(todolist[index - 1].toString());
    }

    private void showList() {
        this.speak("Here is your to-do list:");

        for (int i = 0; i < todoPointer; i++) {
            this.speak((i + 1) + ". " + todolist[i]);
        }
    }

    public static void main(String[] args) {
        Bibo bibo = new Bibo();
        bibo.speak(bibo.biboSays);
        bibo.speak("Hello! I'm Bibo.\nWhat can I do for you?");
        bibo.commands();
        bibo.speak("Bye. Hope to see you again soon!");
    }
}