package dev.tricht.lunaris.tooltip.elements;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Label implements Element {

    private String text;
    private Color color = Color.WHITE;

    public Label(String text) {
        this.text = text;
    }

    public Label(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public Node build() {
        javafx.scene.control.Label label = new javafx.scene.control.Label(text);
        label.setTextFill(color);
        label.setFont(new Font("Arial", 12));
        label.setBackground(new Background(new BackgroundFill(Color.rgb(33, 33, 33), CornerRadii.EMPTY, Insets.EMPTY)));
        label.setPadding(new Insets(10, 10, 10, 10));
        return label;
    }
}
