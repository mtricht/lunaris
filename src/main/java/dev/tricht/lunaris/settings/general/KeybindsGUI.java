package dev.tricht.lunaris.settings.general;

import dev.tricht.lunaris.util.PropertiesManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class KeybindsGUI implements Initializable {
    @FXML
    private TextField priceCheckKeybindInput;

    @FXML
    private TextField searchTradeKeybindInput;

    @FXML
    private TextField itemInfoKeybindInput;

    @FXML
    private TextField hideoutKeybindInput;

    @FXML
    private TextField wikiKeybindInput;


    public KeybindsGUI(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!PropertiesManager.containsKey("keybinds.price_check")) {
            PropertiesManager.writeProperty("keybinds.price_check", "Alt+D");
        }
        if (!PropertiesManager.containsKey("keybinds.search_trade")) {
            PropertiesManager.writeProperty("keybinds.search_trade", "Alt+Q");
        }
        if (!PropertiesManager.containsKey("keybinds.item_info")) {
            PropertiesManager.writeProperty("keybinds.item_info", "Alt+A");
        }
        if (!PropertiesManager.containsKey("keybinds.hideout")) {
            PropertiesManager.writeProperty("keybinds.hideout", "F5");
        }
        if (!PropertiesManager.containsKey("keybinds.wiki")) {
            PropertiesManager.writeProperty("keybinds.wiki", "Alt+W");
        }

        priceCheckKeybindInput.setText(PropertiesManager.getProperty("keybinds.price_check"));
        searchTradeKeybindInput.setText(PropertiesManager.getProperty("keybinds.search_trade"));
        itemInfoKeybindInput.setText(PropertiesManager.getProperty("keybinds.item_info"));
        hideoutKeybindInput.setText(PropertiesManager.getProperty("keybinds.search_trade"));
        wikiKeybindInput.setText(PropertiesManager.getProperty("keybinds.wiki"));
    }
}
