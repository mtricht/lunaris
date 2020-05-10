package dev.tricht.lunaris.tooltip.elements;

import dev.tricht.lunaris.item.Item;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Icon implements Element {

    private Item item;
    private double size;

    public static final double PADDING = UIWrap.PADDING;

    public Icon(Item item, double size) {
        this.item = item;
        this.size = size;
    }

    @Override
    public Node build() {

        Node node;

        try {
            Image image = new Image(item.getIconUrl(), true);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(true);
            node = imageView;
        } catch (NullPointerException | IllegalArgumentException e) {
            node = new Rectangle(size, size, Color.rgb(33, 33, 33, 1));
        }

        return new UIWrap(node, size, size).build();
    }
}
