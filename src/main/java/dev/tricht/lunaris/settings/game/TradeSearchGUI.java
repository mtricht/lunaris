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
    public CheckBox searchInPoeNinja;
    public CheckBox searchInPoePrices;
    public TextField rangeSearchPercentage;
    public CheckBox toggleRangeSearchMinValue;

    public TradeSearchGUI() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        togglePseudoMods.setSelected(PropertiesManager.getProperty("trade_search.pseudo_mods").equals("1"));
        toggleRangeSearch.setSelected(PropertiesManager.getProperty("trade_search.range_search").equals("1"));
        toggleRangeSearchMinValue.setSelected(PropertiesManager.getProperty("trade_search.range_search_only_min").equals("1"));
        searchInPoeNinja.setSelected(PropertiesManager.getProperty("trade_search.poe_ninja").equals("1"));
        searchInPoePrices.setSelected(PropertiesManager.getProperty("trade_search.poeprices").equals("1"));

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
        if (source.getId().equals("searchInPoeNinja")) {
            PropertiesManager.writeProperty("trade_search.poe_ninja", source.isSelected() ? "1" : "0");
        }
        if (source.getId().equals("searchInPoePrices")) {
            PropertiesManager.writeProperty("trade_search.poeprices", source.isSelected() ? "1" : "0");
        }
    }
}
