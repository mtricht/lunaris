package dev.tricht.lunaris.tooltip.elements;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UIWrap implements Element {

    private Node node;
    private double width;
    private double height;

    public static double PADDING = 10;

    public UIWrap(Node node, double width, double height) {
        this.node = node;
        this.width = width;
        this.height = height;
    }

    @Override
    public Node build() {
        StackPane stack = new StackPane();

        Rectangle rect = new Rectangle(width + PADDING, height + PADDING, Color.rgb(33, 33, 33, 1));

        stack.getChildren().add(rect);
        stack.getChildren().add(node);

        return stack;
    }
}
