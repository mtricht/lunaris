package dev.tricht.lunaris.settings.general;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.util.Properties;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
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
        for (String league : PathOfExileAPI.getTradeLeagues()) {
            leagueSelect.getItems().add(league);
        }
        leagueSelect.setValue(Properties.INSTANCE.getProperty(Properties.LEAGUE));
        characterName.setText(Properties.INSTANCE.getProperty(Properties.CHARACTER_NAME));
        poesessid.setText(Properties.INSTANCE.getProperty(Properties.POESESSID));

        leagueSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, t1)
                -> Properties.INSTANCE.writeProperty(Properties.LEAGUE, t1));
        characterName.textProperty().addListener((observableValue, s, t1)
                -> Properties.INSTANCE.writeProperty(Properties.CHARACTER_NAME, t1));
        poesessid.textProperty().addListener((observableValue, s, t1)
                -> Properties.INSTANCE.writeProperty(Properties.POESESSID, t1));
    }
}
