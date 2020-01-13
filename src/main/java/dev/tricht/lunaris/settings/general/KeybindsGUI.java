package dev.tricht.lunaris.settings.general;

import dev.tricht.lunaris.settings.HasSceneContext;
import dev.tricht.lunaris.util.PropertiesManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class KeybindsGUI implements Initializable, HasSceneContext {
    @FXML
    private TextField priceCheckKeybindInput;
    @FXML
    private Button setPriceCheckKey;

    @FXML
    private TextField searchTradeKeybindInput;
    @FXML
    private Button setTradeSearchKey;

    @FXML
    private TextField itemInfoKeybindInput;
    @FXML
    private Button setItemInfoKey;

    @FXML
    private TextField hideoutKeybindInput;
    @FXML
    private Button setHideoutKey;

    @FXML
    private TextField wikiKeybindInput;
    @FXML
    private Button setWikiKey;

    private Scene scene;

    private HashMap<Button, TextField> buttonFields;

    private HashMap<TextField, String> fieldProperties;

    private String comboBeforeChange = null;
    private EventHandler<KeyEvent> eventHandler;

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

        fieldProperties = new HashMap<>();
        fieldProperties.put(priceCheckKeybindInput, "keybinds.price_check");
        fieldProperties.put(searchTradeKeybindInput, "keybinds.search_trade");
        fieldProperties.put(itemInfoKeybindInput, "keybinds.item_info");
        fieldProperties.put(hideoutKeybindInput, "keybinds.hideout");
        fieldProperties.put(wikiKeybindInput, "keybinds.wiki");

        buttonFields = new HashMap<>();
        buttonFields.put(setPriceCheckKey, priceCheckKeybindInput);
        buttonFields.put(setTradeSearchKey, searchTradeKeybindInput);
        buttonFields.put(setItemInfoKey, itemInfoKeybindInput);
        buttonFields.put(setHideoutKey, hideoutKeybindInput);
        buttonFields.put(setWikiKey, wikiKeybindInput);

        for(Map.Entry<TextField, String> entry : fieldProperties.entrySet()) {
            entry.getKey().setText(PropertiesManager.getProperty(entry.getValue()));
        }
    }

    public void setKey(ActionEvent actionEvent) {
        Button button = ((Button) actionEvent.getSource());
        TextField field = buttonFields.get(button);
        if (button.getText().equals("Cancel")) {
            System.out.println(" cancel");
            scene.removeEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
            button.setText("Set key");
            if (comboBeforeChange != null) {
                field.setText(comboBeforeChange);
            } else {
                field.setText("");
            }
            return;
        }

        ArrayList<String> keys = new ArrayList<>();
        AtomicReference<String> keyComboText = new AtomicReference<>("");

        comboBeforeChange = field.getText();
        ((Button) actionEvent.getSource()).setText("Cancel");

        eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().isModifierKey()) {
                    keys.add(keyEvent.getCode().getName());
                    String combo = "";
                    if(keys.contains("Shift")) {
                        combo += "Shift+";
                    }
                    if(keys.contains("Ctrl")) {
                        combo += "Ctrl+";
                    }
                    if(keys.contains("Alt")) {
                        combo += "Alt+";
                    }
                    field.setText(combo);
                    return;
                }

                String finalCombo = "";
                if(keys.contains("Shift")) {
                    finalCombo += "Shift+";
                }
                if(keys.contains("Ctrl")) {
                    finalCombo += "Ctrl+";
                }
                if(keys.contains("Alt")) {
                    finalCombo += "Alt+";
                }
                finalCombo += keyEvent.getCode().getName();
                field.setText(finalCombo);

                PropertiesManager.writeProperty(fieldProperties.get(field), finalCombo);

                scene.removeEventHandler(KeyEvent.KEY_PRESSED, this);

                comboBeforeChange = null;
                ((Button) actionEvent.getSource()).setText("Set key");
            }
        };

        scene.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
    }

    @Override
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
