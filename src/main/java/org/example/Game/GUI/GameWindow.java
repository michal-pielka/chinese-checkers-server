package org.example.Game.GUI;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.Game.Board.Board;
import org.example.Client.Client;

/**
 * A client-side window that displays the board using CircleManager,
 * and allows click-to-move for the current player.
 */
public class GameWindow {

    /**
     * A reference to the Client object, allowing sending of commands.
     */
    private final Client client;

    /**
     * A local copy of the game board for visual updates.
     */
    private final Board localBoard;

    /**
     * The 1-based ID of the current player using this GUI.
     */
    private final int myPlayerId;

    /**
     * The username for this client.
     */
    private final String myPlayerName;

    /**
     * Manages Circle objects (one per node) and their color bindings.
     */
    private final CircleManager circleManager;

    /**
     * Tracks the selected start coordinate (if any) during a move.
     */
    private String selectedStart = null;

    /**
     * The JavaFX Stage displaying the game window.
     */
    private Stage stage;

    /**
     * Constructor that includes both the player's ID and username.
     *
     * @param client       The Client instance to send commands to the server.
     * @param board        A local copy of the board to display.
     * @param myPlayerId   The 1-based ID of the current player.
     * @param myPlayerName The username of the current player.
     */
    public GameWindow(Client client, Board board, int myPlayerId, String myPlayerName) {
        this.client = client;
        this.localBoard = board;
        this.myPlayerId = myPlayerId;
        this.myPlayerName = myPlayerName;

        // Create the CircleManager
        circleManager = new CircleManager();
        circleManager.initializeCircles(localBoard);

        // Add click handlers to each circle
        for (Circle circle : circleManager.getCircles()) {
            circle.setOnMouseClicked(event -> handleCircleClick(circle));
        }
    }

    /**
     * Show the board in a new Stage.
     */
    public void show() {
        stage = new Stage();
        Pane pane = new Pane();
        pane.getChildren().addAll(circleManager.getCircles());

        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);

        // Instead of "Player 1" or "Player 2", we show the actual username
        stage.setTitle("Chinese Checkers - " + myPlayerName);
        stage.show();
    }

    /**
     * Called from the client code whenever the server
     * sends a line relevant to the in-game phase.
     *
     * @param line The message line from the server.
     */
    public void handleServerMessage(String line) {
        // If it says "Moved from x1:y1 to x2:y2", parse and update
        if (line.startsWith("Moved from ")) {
            // e.g. "Moved from 4:3 to 5:4"
            String[] parts = line.split("\\s+");
            if (parts.length == 5) {
                String startPos = parts[2];  // "4:3"
                String endPos   = parts[4];  // "5:4"
                updateLocalBoardMove(startPos, endPos);
            }
        }
    }

    /**
     * When the user clicks a circle, decide if it's the start or the end of a move,
     * and send the move command to the server if both are selected.
     *
     * @param circle The clicked Circle object.
     */
    private void handleCircleClick(Circle circle) {
        String coords = (String) circle.getProperties().get("coords");
        if (coords == null) return;

        if (selectedStart == null) {
            // We haven't selected a start yet, so let's see if the occupant is my player
            int occupant = localBoard.getNode(coords).getPlayer();
            if (occupant == myPlayerId) {
                // Mark this as start
                selectedStart = coords;
                highlightCircle(circle, true);
            }
        } else {
            // We already have a start, so treat this as the end
            String selectedEnd = coords;

            // Send the move command to the server
            client.sendToServer("move " + selectedStart + " " + selectedEnd);

            // Unhighlight the start circle
            unhighlightCircle(selectedStart);
            selectedStart = null;
        }
    }

    /**
     * After the server announces "Moved from X to Y", update localBoard
     * so that CircleManager re-colors accordingly.
     *
     * @param startPos The starting coordinate (e.g., "4:3").
     * @param endPos   The ending coordinate (e.g., "5:4").
     */
    private void updateLocalBoardMove(String startPos, String endPos) {
        String[] sParts = startPos.split(":");
        String[] eParts = endPos.split(":");
        if (sParts.length == 2 && eParts.length == 2) {
            int x1 = Integer.parseInt(sParts[0]);
            int y1 = Integer.parseInt(sParts[1]);
            int x2 = Integer.parseInt(eParts[0]);
            int y2 = Integer.parseInt(eParts[1]);

            // Use board's movePeg
            localBoard.movePeg(x1, y1, x2, y2);
            // Circle color changes automatically because of node.playerProperty() binding
        }
    }

    /**
     * Highlights or un-highlights a circle visually (e.g., using stroke color).
     *
     * @param circle   The circle to highlight.
     * @param highlight Whether to apply highlight (true) or remove it (false).
     */
    private void highlightCircle(Circle circle, boolean highlight) {

        if (highlight) {
            if (!circle.getProperties().containsKey("originalStroke")) {
                circle.getProperties().put("originalStroke", circle.getStroke());
                circle.getProperties().put("originalStrokeWidth", circle.getStrokeWidth());
            }

            circle.setStroke(javafx.scene.paint.Color.YELLOW);
            circle.setStrokeWidth(3);
        } else {
            if (circle.getProperties().containsKey("originalStroke")) {
                circle.setStroke((Color) circle.getProperties().get("originalStroke"));
                circle.setStrokeWidth((double) circle.getProperties().get("originalStrokeWidth"));
            }
        }
    }

    /**
     * Unhighlights a circle by its coordinate key, removing any visual highlight.
     *
     * @param coords The "x:y" coordinate string for the node.
     */
    private void unhighlightCircle(String coords) {
        // Loop all circles to find the one with matching coords
        for (Circle c : circleManager.getCircles()) {
            String cCoords = (String) c.getProperties().get("coords");
            if (coords.equals(cCoords)) {
                highlightCircle(c, false);
                return;
            }
        }
    }
}
