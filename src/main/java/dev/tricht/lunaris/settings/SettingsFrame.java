package dev.tricht.lunaris.settings;

import dev.tricht.lunaris.elements.Element;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.IOException;
import java.util.Map;


public class SettingsFrame extends JFXPanel {
    private Parent root;

    public void init() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings/settings_gui.fxml"));
            root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("css/settings.css");

            loader.<HasSceneContext>getController().setScene(scene);

            setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bounds getLayoutBounds() {
        return root.getLayoutBounds();
    }
}
