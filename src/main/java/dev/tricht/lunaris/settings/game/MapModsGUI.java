package dev.tricht.lunaris.settings.game;

import dev.tricht.lunaris.util.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MapModsGUI implements Initializable {
    @FXML
    public CheckBox lowRecoveryModWarning;
    @FXML
    private CheckBox eleReflModWarning;
    @FXML
    private CheckBox physReflModWarning;
    @FXML
    private CheckBox noLeechModWarning;
    @FXML
    private CheckBox noRegenModWarning;
    @FXML
    private CheckBox multiDmgModWarning;
    @FXML
    private CheckBox tempChainsModWarning;
    @FXML
    private CheckBox avoidElementalAilments;
    @FXML
    private CheckBox avoidPoisonBlindAndBleeding;

    private HashMap<CheckBox, String> propertyMap;

    public MapModsGUI() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        propertyMap = new HashMap<>();

        propertyMap.put(eleReflModWarning, "map_mod_warnings.ele_refl");
        propertyMap.put(physReflModWarning, "map_mod_warnings.phys_refl");
        propertyMap.put(noLeechModWarning, "map_mod_warnings.no_leech");
        propertyMap.put(noRegenModWarning, "map_mod_warnings.no_regen");
        propertyMap.put(multiDmgModWarning, "map_mod_warnings.multi_dmg");
        propertyMap.put(tempChainsModWarning, "map_mod_warnings.tmp_chains");
        propertyMap.put(lowRecoveryModWarning, "map_mod_warnings.low_recovery");
        propertyMap.put(avoidElementalAilments, "map_mod_warnings.avoid_elemental_ailments");
        propertyMap.put(avoidPoisonBlindAndBleeding, "map_mod_warnings.avoid_poison_blind_and_bleed");

        for (Map.Entry<CheckBox, String> entry :propertyMap.entrySet()) {
            entry.getKey().setSelected(Properties.INSTANCE.getProperty(entry.getValue()).equals("1"));
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
