package dev.tricht.poe.assistant.tooltip;

import dev.tricht.poe.assistant.elements.*;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.awt.*;
import java.util.Map;

public class Tooltip extends JFXPanel {

    private TextFlow textFlow;

    public void init(Map<Element, int[]> elements) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        elements.forEach((element, pos) -> gridPane.add(element.build(), pos[0], pos[1]));

        gridPane.setBackground(new Background(new BackgroundFill(Color.rgb(33, 33, 33, 0), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(gridPane);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
