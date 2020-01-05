package dev.tricht.poe.assistant.elements;

import dev.tricht.poe.assistant.item.Item;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ItemName implements Element {

    private Item item;
    private double height;

    static final double SIZE = 32 + UIWrap.PADDING;

    public ItemName(Item item, double height) {
        this.item = item;
        this.height = height;
    }

    @Override
    public Node build() {
        Label label = new Label(item.getName());
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Arial", 18));
        label.setBackground(new Background(new BackgroundFill(Color.rgb(33, 33, 33), CornerRadii.EMPTY, Insets.EMPTY)));
        label.setPadding(new Insets(10, 10, 10, 10));
        label.setMinHeight(height);

        return label;
    }
}
