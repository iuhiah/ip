package bibo.controllers;

import java.util.Timer;
import java.util.TimerTask;

import bibo.Bibo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Bibo bibo;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image biboImage = new Image(this.getClass().getResourceAsStream("/images/bibo.png"));

    /**
     * Initializes main window.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        String greeting = "Beep boop! Bibo!";
        dialogContainer.getChildren().add(
                DialogBox.getBiboDialog(greeting, biboImage)
        );
    }

    /** Injects the Bibo instance */
    public void setBibo(Bibo bibo) {
        this.bibo = bibo;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        String response = bibo.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBiboDialog(response, biboImage)
        );

        userInput.clear();

        if (input.equals("bye")) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Platform.exit();
                }
            };
            new Timer().schedule(task, 1000);
        }
    }
}
