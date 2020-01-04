package dev.tricht.poe.assistant.tooltip;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Tooltip extends JFXPanel {

    private TextFlow textFlow;

    public void init(String message) {
        Text text = new Text(message);
        textFlow = new TextFlow(text);
        textFlow.setPrefWidth(getMaxWidth(message));
        Scene scene = new Scene(textFlow);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
    }

    private int getMaxWidth(String message) {
        String[] lines = message.split("\\r?\\n");
        int max = 0;
        for (String line : lines) {
            int length = line.length() * 6;
            if (length > max) {
                max = length;
            }
        }
        return max;
    }

    public Bounds getTextLayoutBounds() {
        return this.textFlow.getBoundsInLocal();
    }

}
