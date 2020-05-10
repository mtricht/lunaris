package dev.tricht.lunaris.tooltip.elements;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Image implements Element {

    private String url;

    public Image(String url) {
        this.url = url;
    }

    @Override
    public Node build() {
        Node node;
        try {
            javafx.scene.image.Image image = new javafx.scene.image.Image(this.url, true);
            ImageView view = new ImageView(image);
            view.setFitWidth(316);
            view.setFitHeight(164);
            view.setPreserveRatio(true);
            node = view;
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("Failed to create image", e);
            node = new Rectangle(0, 0, Color.rgb(33, 33, 33, 1));
        }

        return new UIWrap(node,316, 164).build();
    }
}
