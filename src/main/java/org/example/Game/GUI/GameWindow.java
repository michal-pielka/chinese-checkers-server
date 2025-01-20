package org.example.Game.GUI;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.Game.Board.Board;
import org.example.Client.Client;

/**
 * A client-side window that displays the board using CircleManager,
 * and allows click-to-move for the current player.
 */
public class GameWindow {

    private final Client client;
    private final Board localBoard;  // copy of the board for local updates
    private final int myPlayerId;    // which player is this client?
    private final String myPlayerName; // the username for this client

    private final CircleManager circleManager;

    // Track the selected start coordinate
    private String selectedStart = null;

    // The JavaFX Stage for this game window
    private Stage stage;

    /**
     * Constructor that includes both the player's ID and username.
     */
    public GameWindow(Client client, Board board, int myPlayerId, String myPlayerName) {
        this.client = client;
        this.localBoard = board;
        this.myPlayerId = myPlayerId;
        this.myPlayerName = myPlayerName; // <-- store the actual name

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
     * Called from your client or lobby code whenever the server
     * sends a line relevant to the in-game phase.
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
        else if (line.contains("Not your turn")) {
            // Possibly show an alert or label
        }
        else if (line.contains("This move is not valid")) {
            // Possibly show an alert
        }
    }

    /**
     * When the user clicks a circle, decide if it's the start or the end of a move.
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

    private void highlightCircle(Circle circle, boolean highlight) {
        if (highlight) {
            circle.setStroke(javafx.scene.paint.Color.YELLOW);
            circle.setStrokeWidth(3);
        } else {
            circle.setStroke(circle.getFill());
            circle.setStrokeWidth(2);
        }
    }

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
