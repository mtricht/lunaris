package dev.tricht.lunaris.settings.general;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.util.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
public class GeneralGUI implements Initializable {

    @FXML
    private ChoiceBox<String> leagueSelect;
    @FXML
    private TextField poesessid;
    @FXML
    private TextField characterName;
    @FXML
    private CheckBox vulcanFix;

    private HashMap<CheckBox, String> propertyMap;

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
                -> Properties.setLeague(t1));
        characterName.textProperty().addListener((observableValue, s, t1)
                -> Properties.INSTANCE.writeProperty(Properties.CHARACTER_NAME, t1));
        poesessid.textProperty().addListener((observableValue, s, t1)
                -> Properties.INSTANCE.writeProperty(Properties.POESESSID, t1));

        propertyMap = new HashMap<>();
        propertyMap.put(vulcanFix, "general.vulcan_fix");
        for (Map.Entry<CheckBox, String> entry :propertyMap.entrySet()) {
            entry.getKey().setSelected(Properties.INSTANCE.getProperty(entry.getValue(), "0").equals("1"));
        }
    }

    public void toggleCheckbox(ActionEvent actionEvent) {
        CheckBox source = (CheckBox) actionEvent.getSource();
        if (!propertyMap.containsKey(source)) {
            return;
        }
        Properties.INSTANCE.writeProperty(propertyMap.get(source), source.isSelected() ? "1" : "0");
    }
}
