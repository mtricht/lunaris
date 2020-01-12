package dev.tricht.lunaris.settings.general;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GeneralGUI implements Initializable {

    @FXML
    private ChoiceBox<String> leagueSelect;

    public GeneralGUI(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leagueSelect.getItems().add("Standard");
        leagueSelect.getItems().add("Hardcore");
        leagueSelect.getItems().add("Metamorph");
        leagueSelect.getItems().add("Hardcore Metamorph");
    }
}
