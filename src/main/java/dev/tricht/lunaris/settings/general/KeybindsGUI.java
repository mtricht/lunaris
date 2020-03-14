package dev.tricht.lunaris.settings.general;

import dev.tricht.lunaris.settings.HasSceneContext;
import dev.tricht.lunaris.util.Properties;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class KeybindsGUI implements Initializable, HasSceneContext {
    @FXML
    private CheckBox toggleStashScroll;

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

    @FXML
    private TextField kickKeybindInput;
    @FXML
    private Button setKickKey;

    @FXML
    private TextField inviteLastWhisperKeybindInput;
    @FXML
    private Button setInviteLastWhisperKeybindInput;

    private Scene scene;

    private HashMap<Button, TextField> buttonFields;

    private HashMap<TextField, String> fieldProperties;

    private String comboBeforeChange = null;
    private EventHandler<KeyEvent> eventHandler;

    public KeybindsGUI(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fieldProperties = new HashMap<>();
        fieldProperties.put(priceCheckKeybindInput, "keybinds.price_check");
        fieldProperties.put(searchTradeKeybindInput, "keybinds.search_trade");
        fieldProperties.put(itemInfoKeybindInput, "keybinds.item_info");
        fieldProperties.put(hideoutKeybindInput, "keybinds.hideout");
        fieldProperties.put(wikiKeybindInput, "keybinds.wiki");
        fieldProperties.put(kickKeybindInput, "keybinds.kick");
        fieldProperties.put(inviteLastWhisperKeybindInput, "keybinds.invite_last_whisper");

        buttonFields = new HashMap<>();
        buttonFields.put(setPriceCheckKey, priceCheckKeybindInput);
        buttonFields.put(setTradeSearchKey, searchTradeKeybindInput);
        buttonFields.put(setItemInfoKey, itemInfoKeybindInput);
        buttonFields.put(setHideoutKey, hideoutKeybindInput);
        buttonFields.put(setWikiKey, wikiKeybindInput);
        buttonFields.put(setKickKey, kickKeybindInput);
        buttonFields.put(setInviteLastWhisperKeybindInput, inviteLastWhisperKeybindInput);

        for(Map.Entry<TextField, String> entry : fieldProperties.entrySet()) {
            entry.getKey().setText(Properties.INSTANCE.getProperty(entry.getValue()));
        }

        toggleStashScroll.setSelected(Properties.INSTANCE.getProperty("keybinds.enable_stash_scroll").equals("1"));
    }

    public void setKey(ActionEvent actionEvent) {
        Button button = ((Button) actionEvent.getSource());
        TextField field = buttonFields.get(button);
        if (button.getText().equals("Cancel")) {
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

                Properties.INSTANCE.writeProperty(fieldProperties.get(field), finalCombo);

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

    public void toggleCheckbox(ActionEvent actionEvent) {
        Properties.INSTANCE.writeProperty("keybinds.enable_stash_scroll", ((CheckBox)actionEvent.getSource()).isSelected() ? "1" : "0");
    }
}
