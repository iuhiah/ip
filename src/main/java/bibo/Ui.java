package bibo;

import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a user interface that interacts with the user.
 */
public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a formatted message to the console.
     *
     * @param message Message to print.
     */
    protected void speak(String message) {
        System.out.println("\n---------- Bibo says: ----------");
        System.out.println(message);
    }

    /**
     * Reads user input from console.
     *
     * @return User input.
     * @throws IOException
     */
    protected String getCommand() throws IOException {
        System.out.println("\n----------- You say: -----------");
        String input = scanner.nextLine();

        // for logging purposes, if input is redirected
        if (System.console() == null) {
            // echo input to console
            System.out.println(input);
        }

        return input;
    }

    /**
     * Opens the scanner and greets the user.
     */
    protected void open() {
        speak("Hello! I'm Bibo. What can I do for you today?");
    }

    /**
     * Closes the scanner.
     */
    protected void close() {
        scanner.close();
        speak("Bye. Hope to see you again soon!");
    }
}
