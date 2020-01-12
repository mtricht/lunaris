package dev.tricht.lunaris.settings;

import dev.tricht.lunaris.util.PropertiesManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestUI extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        PropertiesManager.load();
        Parent root = FXMLLoader.load(getClass().getResource("/settings/settings_gui.fxml"));
        primaryStage.setTitle("My First JavaFX App");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/settings.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}