package dev.tricht.lunaris.settings;

import com.sun.source.tree.Tree;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SettingsGUI implements Initializable {

    @FXML
    private AnchorPane settingsPane;
    @FXML
    private TreeView<String> settingsTree;


    private HashMap<TreeItem<String>, Parent> treeItemListeners;

    public SettingsGUI() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TreeItem<String> rootItem = new TreeItem<>("Settings");

        TreeItem<String> generalItem = new TreeItem<>("General");
        TreeItem<String> keybindsItem = new TreeItem<>(" Keybinds");
        generalItem.getChildren().add(keybindsItem);


        TreeItem<String> gameItem = new TreeItem<>("Game");
        TreeItem<String> mapModsItem = new TreeItem<>(" Map mods");
        gameItem.getChildren().add(mapModsItem);

        TreeItem<String> aboutItem = new TreeItem<>("About");

        rootItem.getChildren().add(generalItem);
        rootItem.getChildren().add(gameItem);
        rootItem.getChildren().add(aboutItem);
        settingsTree.setRoot(rootItem);
        settingsTree.setShowRoot(false);

        treeItemListeners = new HashMap<>();
        try {
            Parent keybindsPane = FXMLLoader.load(getClass().getResource("/settings/general/keybinds.fxml"));
            treeItemListeners.put(keybindsItem, keybindsPane);

            Parent mapModsPane = FXMLLoader.load(getClass().getResource("/settings/game/map_mods.fxml"));
            treeItemListeners.put(mapModsItem, mapModsPane);

            Parent generalPane = FXMLLoader.load(getClass().getResource("/settings/general/general.fxml"));
            treeItemListeners.put(generalItem, generalPane);

        } catch (IOException e) {
            e.printStackTrace();
        }

        settingsTree.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, t1) -> {

            if (treeItemListeners.containsKey(t1)) {
                settingsPane.getChildren().setAll(treeItemListeners.get(t1));
            }
            System.out.println("Selected Text : " + t1.getValue());
        });
    }
}