package dev.tricht.lunaris.settings;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;


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
