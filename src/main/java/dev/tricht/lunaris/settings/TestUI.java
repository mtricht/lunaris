package dev.tricht.lunaris.settings;

import dev.tricht.lunaris.com.pathofexile.Leagues;
import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
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
        Leagues.load(new PathOfExileAPI());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings/settings_gui.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("Lunaris");

        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/settings.css");

        loader.<HasSceneContext>getController().setScene(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}