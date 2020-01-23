package dev.tricht.lunaris.settings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SettingsGUI implements Initializable, HasSceneContext {

    @FXML
    private AnchorPane settingsPane;
    @FXML
    private TreeView<String> settingsTree;

    private HashMap<TreeItem<String>, Parent> treeItemListeners;
    private Scene scene;

    private ArrayList<HasSceneContext> sceneControllers;

    public SettingsGUI() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sceneControllers = new ArrayList<>();

        TreeItem<String> rootItem = new TreeItem<>("Settings");

        TreeItem<String> generalItem = new TreeItem<>("General");
        TreeItem<String> keybindsItem = new TreeItem<>("Keybinds");
        generalItem.getChildren().add(keybindsItem);


        TreeItem<String> gameItem = new TreeItem<>("Game");
        TreeItem<String> mapModsItem = new TreeItem<>(" Map mods");
        TreeItem<String> tradeSearchItem = new TreeItem<>("Trade Search");
        gameItem.getChildren().add(tradeSearchItem);
        gameItem.getChildren().add(mapModsItem);

        TreeItem<String> aboutItem = new TreeItem<>("About");

        rootItem.getChildren().add(generalItem);
        rootItem.getChildren().add(gameItem);
        rootItem.getChildren().add(aboutItem);
        settingsTree.setRoot(rootItem);
        settingsTree.setShowRoot(false);

        treeItemListeners = new HashMap<>();
        try {
            FXMLLoader keybindLoader = new FXMLLoader(getClass().getResource("/settings/general/keybinds.fxml"));
            Parent keybindsPane = keybindLoader.load();
            sceneControllers.add(keybindLoader.<HasSceneContext>getController());
            treeItemListeners.put(keybindsItem, keybindsPane);


            FXMLLoader tradeSearchLoader = new FXMLLoader(getClass().getResource("/settings/game/trade_search.fxml"));
            Parent tradeSearchPane = tradeSearchLoader.load();
            treeItemListeners.put(tradeSearchItem, tradeSearchPane);

            FXMLLoader mapModsLoader = new FXMLLoader(getClass().getResource("/settings/game/map_mods.fxml"));
            Parent mapModsPane = mapModsLoader.load();
            treeItemListeners.put(mapModsItem, mapModsPane);

            FXMLLoader generalPaneLoader = new FXMLLoader(getClass().getResource("/settings/general/general.fxml"));
            Parent generalPane = generalPaneLoader.load();
            treeItemListeners.put(generalItem, generalPane);

            FXMLLoader aboutPaneLoader = new FXMLLoader(getClass().getResource("/settings/about.fxml"));
            Parent aboutPane = aboutPaneLoader.load();
            treeItemListeners.put(aboutItem, aboutPane);

        } catch (IOException e) {
            e.printStackTrace();
        }

        settingsTree.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, t1) -> {
            if (treeItemListeners.containsKey(t1)) {
                settingsPane.getChildren().setAll(treeItemListeners.get(t1));
            }
        });

        settingsTree.getSelectionModel().select(generalItem);
    }

    public void setScene(Scene scene) {
        for (HasSceneContext controller : sceneControllers) {
            controller.setScene(scene);
        }
    }
}