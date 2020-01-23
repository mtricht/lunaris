package dev.tricht.lunaris.settings.game;

import dev.tricht.lunaris.util.PropertiesManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TradeSearchGUI implements Initializable {

    public CheckBox togglePseudoMods;
    public CheckBox toggleRangeSearch;
    public TextField rangeSearchPercentage;
    public CheckBox toggleRangeSearchMinValue;

    public TradeSearchGUI() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!PropertiesManager.containsKey("trade_search.pseudo_mods")) {
            PropertiesManager.writeProperty("trade_search.pseudo_mods", "1");
        }
        if (!PropertiesManager.containsKey("trade_search.range_search")) {
            PropertiesManager.writeProperty("trade_search.range_search", "1");
        }
        if (!PropertiesManager.containsKey("trade_search.range_search_percentage")) {
            PropertiesManager.writeProperty("trade_search.range_search_percentage", "20");
        }
        if (!PropertiesManager.containsKey("trade_search.range_search_only_min")) {
            PropertiesManager.writeProperty("trade_search.range_search_only_min", "1");
        }

        togglePseudoMods.setSelected(PropertiesManager.getProperty("trade_search.pseudo_mods").equals("1"));
        toggleRangeSearch.setSelected(PropertiesManager.getProperty("trade_search.range_search").equals("1"));
        toggleRangeSearchMinValue.setSelected(PropertiesManager.getProperty("trade_search.range_search_only_min").equals("1"));

        rangeSearchPercentage.setText(PropertiesManager.getProperty("trade_search.range_search_percentage"));
        rangeSearchPercentage.textProperty().addListener((observableValue, s, t1)
                -> PropertiesManager.writeProperty("trade_search.range_search_percentage", t1));

        toggleRangeSearchMinValue.setDisable(!PropertiesManager.getProperty("trade_search.range_search").equals("1"));
        rangeSearchPercentage.setDisable(!PropertiesManager.getProperty("trade_search.range_search").equals("1"));
    }

    public void toggleCheckbox(ActionEvent actionEvent) {
        CheckBox source = (CheckBox) actionEvent.getSource();
        if (source.getId().equals("togglePseudoMods")) {
            PropertiesManager.writeProperty("trade_search.pseudo_mods", source.isSelected() ? "1" : "0");
        }
        if (source.getId().equals("toggleRangeSearch")) {
            PropertiesManager.writeProperty("trade_search.range_search", source.isSelected() ? "1" : "0");
            rangeSearchPercentage.setDisable(!source.isSelected());
            toggleRangeSearchMinValue.setDisable(!source.isSelected());
        }
        if (source.getId().equals("toggleRangeSearchMinValue")) {
            PropertiesManager.writeProperty("trade_search.range_search_only_min", source.isSelected() ? "1" : "0");
        }
    }
}
