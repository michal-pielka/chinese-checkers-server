package org.example.Game.GUI;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.Game.Board.Board;
import org.example.Game.Board.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages circles for each node on the board, including color bindings
 * to the underlying player's ID in each Node.
 */
public class CircleManager {

    /**
     * A list of all Circle objects representing nodes on the board.
     */
    private final List<Circle> circles;

    /**
     * The radius (in pixels) for each circle (node).
     */
    private final int radius;

    /**
     * Constructs a new CircleManager, initializing internal data structures.
     */
    public CircleManager() {
        circles = new ArrayList<>();
        radius = 12;
    }

    /**
     * Creates all circles for the given board's nodes,
     * binds their color to the node's player ID,
     * and installs a tooltip showing coordinates and occupant.
     *
     * @param board The board whose nodes are to be visualized with circles.
     */
    public void initializeCircles(Board board) {
        for (Node node : board.getNodes().values()) {
            Circle circle = new Circle();
            circle.setRadius(radius);
            circle.setCenterX(calculateXPos(node));
            circle.setCenterY(calculateYPos(node));

            // Store the coords in the circle's properties map
            String coords = node.getX() + ":" + node.getY();
            circle.getProperties().put("coords", coords);

            initColors(circle, node);
            addNodeListener(circle, node);
            addTooltip(circle, node);

            circles.add(circle);
        }
    }

    /**
     * Assign initial color/stroke based on node's player occupant.
     */
    private void initColors(Circle circle, Node node) {
        int player = node.getPlayer();
        switch (player) {
            case 1 -> {
                circle.setFill(Color.BLUE);
                circle.setStroke(Color.BLUE);
            }
            case 2 -> {
                circle.setFill(Color.GREEN);
                circle.setStroke(Color.GREEN);
            }
            case 3 -> {
                circle.setFill(Color.PINK);
                circle.setStroke(Color.PINK);
            }
            case 4 -> {
                circle.setFill(Color.MEDIUMPURPLE);
                circle.setStroke(Color.MEDIUMPURPLE);
            }
            case 5 -> {
                circle.setFill(Color.RED);
                circle.setStroke(Color.RED);
            }
            case 6 -> {
                circle.setFill(Color.ORANGE);
                circle.setStroke(Color.ORANGE);
            }
            default -> {
                circle.setFill(Color.GRAY);
                circle.setStroke(Color.BLACK);
            }
        }
        circle.setStrokeWidth(2);
    }

    /**
     * Adds a listener to the node's player property, updating the circle color in real time
     * whenever the occupant changes.
     */
    private void addNodeListener(Circle circle, Node node) {
        ChangeListener<Number> playerChangeListener = (observable, oldValue, newValue) -> {
            switch (newValue.intValue()) {
                case 1 -> circle.setFill(Color.BLUE);
                case 2 -> circle.setFill(Color.GREEN);
                case 3 -> circle.setFill(Color.PINK);
                case 4 -> circle.setFill(Color.MEDIUMPURPLE);
                case 5 -> circle.setFill(Color.RED);
                case 6 -> circle.setFill(Color.ORANGE);
                default -> circle.setFill(Color.GRAY);
            }
        };

        node.playerProperty().addListener(playerChangeListener);
    }

    /**
     * Adds a Tooltip that shows "X:Y player: N" on hover, updating
     * when the player occupant changes.
     */
    private void addTooltip(Circle circle, Node node) {
        Tooltip tooltip = new Tooltip(node.getX() + ":" + node.getY() + " player: " + node.getPlayer());
        Tooltip.install(circle, tooltip);
        tooltip.setShowDelay(javafx.util.Duration.seconds(0.5));

        node.playerProperty().addListener((observable, oldVal, newVal) -> {
            tooltip.setText(node.getX() + ":" + node.getY() + " player: " + newVal.intValue());
        });
    }

    /**
     * Calculates the X-position for rendering this node in a 2D layout.
     *
     * @param node The node whose coordinates are being mapped.
     * @return The calculated X screen coordinate.
     */
    private int calculateXPos(Node node) {
        int nodeX = node.getX();
        int nodeY = node.getY();
        // This logic depends on how you want to visually place them
        return 400 + (nodeX - 4) * 2 * radius - nodeY * radius;
    }

    /**
     * Calculates the Y-position for rendering this node in a 2D layout.
     *
     * @param node The node whose coordinates are being mapped.
     * @return The calculated Y screen coordinate.
     */
    private int calculateYPos(Node node) {
        int nodeY = node.getY();
        return 100 + nodeY * 2 * radius;
    }

    /**
     * Retrieves the list of all Circle objects managed by this CircleManager.
     *
     * @return The list of circles.
     */
    public List<Circle> getCircles() {
        return circles;
    }
}
