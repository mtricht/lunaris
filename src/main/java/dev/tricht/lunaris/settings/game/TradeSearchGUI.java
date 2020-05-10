package dev.tricht.lunaris.settings.game;

import dev.tricht.lunaris.util.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
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
        togglePseudoMods.setSelected(Properties.INSTANCE.getProperty("trade_search.pseudo_mods").equals("1"));
        toggleRangeSearch.setSelected(Properties.INSTANCE.getProperty("trade_search.range_search").equals("1"));
        toggleRangeSearchMinValue.setSelected(Properties.INSTANCE.getProperty("trade_search.range_search_only_min").equals("1"));
        searchInPoeNinja.setSelected(Properties.INSTANCE.getProperty("trade_search.poe_ninja").equals("1"));
        searchInPoePrices.setSelected(Properties.INSTANCE.getProperty("trade_search.poeprices").equals("1"));

        rangeSearchPercentage.setText(Properties.INSTANCE.getProperty("trade_search.range_search_percentage"));
        rangeSearchPercentage.textProperty().addListener((observableValue, s, t1)
                -> Properties.INSTANCE.writeProperty("trade_search.range_search_percentage", t1));

        toggleRangeSearchMinValue.setDisable(!Properties.INSTANCE.getProperty("trade_search.range_search").equals("1"));
        rangeSearchPercentage.setDisable(!Properties.INSTANCE.getProperty("trade_search.range_search").equals("1"));
    }

    public void toggleCheckbox(ActionEvent actionEvent) {
        CheckBox source = (CheckBox) actionEvent.getSource();
        if (source.getId().equals("togglePseudoMods")) {
            Properties.INSTANCE.writeProperty("trade_search.pseudo_mods", source.isSelected() ? "1" : "0");
        }
        if (source.getId().equals("toggleRangeSearch")) {
            Properties.INSTANCE.writeProperty("trade_search.range_search", source.isSelected() ? "1" : "0");
            rangeSearchPercentage.setDisable(!source.isSelected());
            toggleRangeSearchMinValue.setDisable(!source.isSelected());
        }
        if (source.getId().equals("toggleRangeSearchMinValue")) {
            Properties.INSTANCE.writeProperty("trade_search.range_search_only_min", source.isSelected() ? "1" : "0");
        }
        if (source.getId().equals("searchInPoeNinja")) {
            Properties.INSTANCE.writeProperty("trade_search.poe_ninja", source.isSelected() ? "1" : "0");
        }
        if (source.getId().equals("searchInPoePrices")) {
            Properties.INSTANCE.writeProperty("trade_search.poeprices", source.isSelected() ? "1" : "0");
        }
    }
}
