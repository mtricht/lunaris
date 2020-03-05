package dev.tricht.lunaris.settings;

import dev.tricht.lunaris.AutoUpdateConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

@Slf4j
public class AboutGUI implements Initializable {

    @FXML
    private Label version;

    public AboutGUI(){ }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Properties properties = new Properties();
        try {
            properties.load(AutoUpdateConfiguration.class.getResourceAsStream("/lunaris.properties"));
            version.setText("Version " + properties.getProperty("version"));
        } catch (IOException e) {
            log.error("Failed to load lunaris.properties", e);
        }

    }

}
