package org.example.Game.GUI;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.Game.Board.Board;
import org.example.Game.Board.Node;

import java.util.ArrayList;
import java.util.List;


public class CircleManager {
    List<Circle> circles;
    private final int radius;

    public CircleManager() {
        circles = new ArrayList<>();
        radius = 12;
    }

    public void initializeCircles(Board board) {
        for(Node node : board.getNodes().values()) {
            Circle circle = new Circle();
            circle.setRadius(radius);
            circle.setCenterX(calculateXPos(node));
            circle.setCenterY(calculateYPos(node));
            initColors(circle, node);
            addNodeListener(circle, node);
            addTooltip(circle, node);

            circles.add(circle);
        }

    }

    private void initColors(Circle circle, Node node) {
        int player = node.getPlayer();
        switch (player) {
            case 1:
                circle.setFill(Color.BLUE);
                circle.setStroke(Color.BLUE);
                circle.setStrokeWidth(2);
                break;

            case 2:
                circle.setFill(Color.GREEN);
                circle.setStroke(Color.GREEN);
                circle.setStrokeWidth(2);
                break;

            case 3:
                circle.setFill(Color.PINK);
                circle.setStroke(Color.PINK);
                circle.setStrokeWidth(2);
                break;

            case 4:
                circle.setFill(Color.MEDIUMPURPLE);
                circle.setStroke(Color.MEDIUMPURPLE);
                circle.setStrokeWidth(2);
                break;

            case 5:
                circle.setFill(Color.RED);
                circle.setStroke(Color.RED);
                circle.setStrokeWidth(2);
                break;

            case 6:
                circle.setFill(Color.ORANGE);
                circle.setStroke(Color.ORANGE);
                circle.setStrokeWidth(2);
                break;

            default:
                circle.setFill(Color.GRAY);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(2);
                break;
        }

    }

    private int calculateXPos(Node node) {
        int nodeX = node.getX();
        int nodeY = node.getY();
        return 400 + (nodeX-4)*2*radius - nodeY*radius;
    }

    private int calculateYPos(Node node) {
        int nodeY = node.getY();
        return 100 + nodeY * 2 * radius;
    }

    private void addNodeListener(Circle circle, Node node) {
        ChangeListener<Number> playerChangeListener = (observable, oldValue, newValue) -> {
            switch (newValue.intValue()) {
                case 1 -> circle.setFill(Color.BLUE);
                case 2 -> circle.setFill(Color.GREEN);
                case 3 -> circle.setFill(Color.PINK);
                case 4 -> circle.setFill(Color.LAVENDER);
                case 5 -> circle.setFill(Color.RED);
                case 6 -> circle.setFill(Color.ORANGE);
                default -> circle.setFill(Color.GRAY);
            }
        };

        node.playerProperty().addListener(playerChangeListener);
    }

    private void addTooltip(Circle circle, Node node) {
        Tooltip tooltip = new Tooltip(node.getX()+":"+node.getY()+ " player: " + node.getPlayer());
        Tooltip.install(circle, tooltip);
        tooltip.setShowDelay(javafx.util.Duration.seconds(0.5));

        node.playerProperty().addListener((observable, oldValue, newValue) -> {
            tooltip.setText(node.getX() + ":" + node.getY() + " player: " + newValue.intValue());
        });
    }

    public List<Circle> getCircles() {
        return circles;
    }
}
