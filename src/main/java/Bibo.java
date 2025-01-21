import java.util.Scanner;

public class Bibo {
    public static void speak(String message) {
        System.out.println("\n---------- Bibo says: ----------");
        System.out.println(message);

        if (message.equals("Bye. Hope to see you again soon!")) {
            return;
        }
        System.out.println("\n----------- You say: -----------");
    }

    public static void main(String[] args) {
        speak("Hello! I'm Bibo.\nWhat can I do for you?");

        // echo input
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            speak(input);
        }

        speak("Bye. Hope to see you again soon!");
    }
}