package bibo;

import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a user interface that interacts with the user.
 */
public class Ui {
    protected boolean isRunning;
    private Scanner scanner;

    /**
     * Initialises a new Ui instance.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    /**
     * Prints formatted Bibo header.
     */
    public void speak() {
        System.out.println("\n---------- Bibo says: ----------");
    }

    /**
     * Prints formatted Bibo header with message.
     *
     * @param message Message to print.
     */
    public void speak(String message) {
        System.out.println("\n---------- Bibo says: ----------");
        System.out.println(message);
    }

    /**
     * Prints message without Bibo header.
     */
    public void join(String message) {
        System.out.println(message);
    }

    /**
     * Reads user input from console.
     *
     * @return User input.
     * @throws IOException
     */
    public String getInput() throws IOException {
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
    public void open() {
        speak("Hello! I'm Bibo. What can I do for you today?");
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
        speak("Bye. Hope to see you again soon!");
        isRunning = false;
    }
}
