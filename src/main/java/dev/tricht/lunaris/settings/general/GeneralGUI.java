package dev.tricht.lunaris.settings.general;

import dev.tricht.lunaris.com.pathofexile.Leagues;
import dev.tricht.lunaris.util.PropertiesManager;
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
        for (String league : Leagues.getLeagues()) {
            leagueSelect.getItems().add(league);
        }
        leagueSelect.setValue(PropertiesManager.getProperty("LEAGUE"));

        leagueSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, t1) -> {
            PropertiesManager.writeProperty("LEAGUE", t1);
        });
    }
}
