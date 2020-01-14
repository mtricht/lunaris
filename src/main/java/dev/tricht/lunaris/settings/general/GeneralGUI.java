package dev.tricht.lunaris.settings.general;

import dev.tricht.lunaris.com.pathofexile.Leagues;
import dev.tricht.lunaris.util.PropertiesManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GeneralGUI implements Initializable {

    @FXML
    private ChoiceBox<String> leagueSelect;
    @FXML
    private TextField poesessid;
    @FXML
    private TextField characterName;

    public GeneralGUI(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (String league : Leagues.getLeagues()) {
            leagueSelect.getItems().add(league);
        }
        leagueSelect.setValue(PropertiesManager.getProperty(PropertiesManager.LEAGUE));
        characterName.setText(PropertiesManager.getProperty(PropertiesManager.CHARACTER_NAME));
        poesessid.setText(PropertiesManager.getProperty(PropertiesManager.POESESSID));

        leagueSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, t1)
                -> PropertiesManager.writeProperty(PropertiesManager.LEAGUE, t1));
        characterName.textProperty().addListener((observableValue, s, t1)
                -> PropertiesManager.writeProperty(PropertiesManager.CHARACTER_NAME, t1));
        poesessid.textProperty().addListener((observableValue, s, t1)
                -> PropertiesManager.writeProperty(PropertiesManager.POESESSID, t1));
    }
}
