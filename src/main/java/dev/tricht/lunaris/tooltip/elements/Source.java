package dev.tricht.lunaris.tooltip.elements;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Source implements Element {

    private String source;

    public Source(String source) {
        this.source = source;
    }

    @Override
    public Node build() {
        Label label = new Label(String.format("from: %s", source));
        label.setTextFill(Color.SLATEGRAY);
        label.setFont(new Font("Arial", 11));
        label.setBackground(new Background(new BackgroundFill(Color.rgb(33, 33, 33), CornerRadii.EMPTY, Insets.EMPTY)));
        label.setPadding(new Insets(4, 4, 4, 4));

        return label;
    }
}
