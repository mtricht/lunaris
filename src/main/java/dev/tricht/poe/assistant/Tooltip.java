package dev.tricht.poe.assistant;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

class Tooltip extends JFXPanel {

    private Text text;

    void init(String message) {
        text = new Text(message);
        Scene scene = new Scene(new TextFlow(text));
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
    }

    Bounds getTextLayoutBounds() {
        return this.text.getLayoutBounds();
    }

}
