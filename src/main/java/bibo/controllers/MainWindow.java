package bibo.controllers;

import java.util.Timer;
import java.util.TimerTask;

import bibo.Bibo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
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
    private ImageView biboImg;
    @FXML
    private ImageView userImg;

    private Bibo bibo;

    /**
     * Initializes main window.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        Image img = new Image(getClass().getResource("/images/background.png").toExternalForm());
        BackgroundImage bgImg = new BackgroundImage(img,
                javafx.scene.layout.BackgroundRepeat.NO_REPEAT,
                javafx.scene.layout.BackgroundRepeat.NO_REPEAT,
                javafx.scene.layout.BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        dialogContainer.setBackground(new Background(bgImg));

        dialogContainer.getChildren().add(
            DialogBox.getBiboDialog("Beep boop! Bibo!")
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
                DialogBox.getUserDialog(input),
                DialogBox.getBiboDialog(response)
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
