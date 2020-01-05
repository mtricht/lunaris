package dev.tricht.poe.assistant.elements;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Label implements Element {

    private String text;

    public Label(String text) {
        this.text = text;
    }

    @Override
    public Node build() {
        javafx.scene.control.Label label = new javafx.scene.control.Label(text);
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(new Font("Arial", 12));
        label.setBackground(new Background(new BackgroundFill(Color.rgb(33, 33, 33), CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        label.setPadding(new Insets(10, 10, 10, 10));
        return label;
    }
}
