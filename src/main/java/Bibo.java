import java.util.Scanner;

public class Bibo {
    public static void speak(String message) {
        System.out.println(message);
    }

    private static Task[] todolist = new Task[100];
    private static int todolistCurr = 0;

    public static void add(String task) {
        todolist[todolistCurr] = new Task(task);
        todolistCurr++;

        speak("I've added [" + task + "] to your to-do list!");
    }

    public static void showList() {
        speak("Here is your to-do list:");

        for (int i = 0; i < todolistCurr; i++) {
            speak((i + 1) + ". " + todolist[i]);
        }
    }

    public static void markTask(int index) {
        if (index < 1 || index > todolistCurr) {
            speak("That's not in your to-do list! Try something else.");
            return;
        }

        todolist[index - 1].markAsDone();
        speak("Nice! I've marked this task as done:");
        speak(todolist[index - 1].toString());
    }

    public static void unmarkTask(int index) {
        if (index < 1 || index > todolistCurr) {
            speak("That's not in your to-do list! Try something else.");
            return;
        }

        todolist[index - 1].markAsUndone();
        speak("Alright! I've unmarked this task:");
        speak(todolist[index - 1].toString());
    }

    public static void main(String[] args) {
        speak("Hello! I'm Bibo.\nWhat can I do for you?");

        while (true) {
            speak("\n----------- You say: -----------");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            speak("\n---------- Bibo says: ----------");

            if (input.equals("bye")) {
                break;
            }
            
            if (input.equals("list")) {
                showList();
            } else if (input.startsWith("mark")) {
                String[] words = input.split(" ");
                int index = Integer.parseInt(words[1]);
                markTask(index);
            } else if (input.startsWith("unmark")) {
                String[] words = input.split(" ");
                int index = Integer.parseInt(words[1]);
                unmarkTask(index);
            } else {
                add(input);
            }

            speak("What's next?");
        }

        speak("\n---------- Bibo says: ----------");
        speak("Bye. Hope to see you again soon!");
    }
}