import java.util.Scanner;

public class Bibo {
    public static void speak(String message) {
        System.out.println("\n---------- Bibo says: ----------");
        System.out.println(message);
        System.out.println("\n----------- You say: -----------");
    }

    // overloading for output formatting
    public static void speak(String message, boolean start, boolean end) {
        if (start) {
            System.out.println("\n---------- Bibo says: ----------");
        }
        
        System.out.println(message);

        if (end) {
            System.out.println("\n----------- You say: -----------");
        }
    }

    private static String[] todolist = new String[100];
    private static int todolistCurr = 0;

    public static void add(String task) {
        todolist[todolistCurr] = task;
        todolistCurr++;

        speak("I've added [" + task + "] to your to-do list! What's next?");
    }

    public static void showList() {
        speak("Here is your current to-do list:", true, false);
        for (int i = 0; i < todolistCurr; i++) {
            System.out.println((i + 1) + ". " + todolist[i]);
        }
        speak("What's next?", false, true);
    }

    public static void main(String[] args) {
        speak("Hello! I'm Bibo.\nWhat can I do for you?");

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                showList();
                continue;
            }
            add(input);
        }

        speak("Bye. Hope to see you again soon!", true, false);
    }
}