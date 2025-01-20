package org.example.Client;

import javafx.application.Application;

/**
 * Entry point for the client application (GUI-based).
 */
public class RunClient {
    /**
     * Main method to launch the JavaFX client GUI.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        // Launch the JavaFX GUI
        Application.launch(ClientGUI.class, args);
    }
}
