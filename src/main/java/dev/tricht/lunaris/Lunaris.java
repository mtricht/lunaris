package dev.tricht.lunaris;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.listeners.*;
import dev.tricht.lunaris.ninja.poe.ItemResolver;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class Lunaris {

    private Robot robot;
    private PathOfExileAPI pathOfExileAPI;
    private ItemGrabber itemGrabber;
    private ItemResolver itemResolver;
    private String selectedLeagueName;
    private ArrayList<CheckboxMenuItem> leagueMenuItems;

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        new Lunaris();
    }

    private Lunaris() {
        pathOfExileAPI = new PathOfExileAPI();
        createSysTray();
        try {
            robot = new Robot();
            itemResolver = new ItemResolver(selectedLeagueName);
            itemGrabber = new ItemGrabber(robot, itemResolver);
        } catch (IOException | AWTException e) {
            log.error("Failed to initialize robot", e);
            return;
        }
        startListeners();
        // For some reason the JavaFX thread will completely stop after closing
        // the first tooltip. Setting this will prevent that from happening.
        Platform.setImplicitExit(false);
        log.debug("Ready!");
    }

    private void startListeners() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            log.error("Failed to start native hooks", e);
            System.exit(1);
            return;
        }

        log.debug("Registering ItemPriceListener");
        ItemPriceListener itemPriceListener = new ItemPriceListener(itemGrabber, pathOfExileAPI);
        GlobalScreen.addNativeKeyListener(itemPriceListener);
        GlobalScreen.addNativeMouseMotionListener(itemPriceListener);
        GlobalScreen.addNativeMouseListener(itemPriceListener);

        log.debug("Registering MapInfoListener");
        MapInfoListener mapInfoListener = new MapInfoListener(itemGrabber);
        GlobalScreen.addNativeKeyListener(mapInfoListener);
        GlobalScreen.addNativeMouseMotionListener(mapInfoListener);
        GlobalScreen.addNativeMouseListener(mapInfoListener);

        log.debug("Registering StashListener");
        StashListener stashListener = new StashListener(robot);
        GlobalScreen.addNativeKeyListener(stashListener);
        GlobalScreen.addNativeMouseWheelListener(stashListener);

        log.debug("Registering WikiListener");
        GlobalScreen.addNativeKeyListener(new WikiListener(itemGrabber));

        log.debug("Registering HideoutListener");
        GlobalScreen.addNativeKeyListener(new HideoutListener(robot));
    }

    private void createSysTray() {
        if (!SystemTray.isSupported()) {
            log.error("SystemTray is not supported");
            return;
        }
        leagueMenuItems = new ArrayList<>();
        final PopupMenu popup = new PopupMenu("Lunaris");
        final TrayIcon trayIcon = new TrayIcon(getIcon());
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();

        Menu leagueMenu = new Menu("League");
        int count = 0;
        for (String leagueName : pathOfExileAPI.getTradeLeagues()) {
            CheckboxMenuItem leagueMenuItem = new CheckboxMenuItem(leagueName);
            leagueMenuItems.add(leagueMenuItem);
            leagueMenu.add(leagueMenuItem);
            leagueMenuItem.addItemListener(this::changeLeague);
            if (count == 2) {
                leagueMenuItem.setState(true);
                this.changeLeague(new ItemEvent(leagueMenuItem, 0, leagueName, ItemEvent.SELECTED));
            }
            count++;
        }

        MenuItem exitItem = new MenuItem("Exit");

        popup.add(leagueMenu);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            log.error("TrayIcon could not be added.");
        }

        exitItem.addActionListener(e -> {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                log.error("Failed to unregister native hook", ex);
            }
            System.exit(0);
        });
    }

    private static Image getIcon() {
        URL imageURL = Lunaris.class.getClassLoader().getResource("icon.png");

        if (imageURL == null) {
            log.error("icon.png missing");
            return null;
        } else {
            return (new ImageIcon(imageURL, "Lunaris")).getImage();
        }
    }

    private void changeLeague(ItemEvent event) {
        String newLeagueName = event.getItem().toString();
        if (selectedLeagueName != null && selectedLeagueName.equals(newLeagueName)) {
            return;
        }
        selectedLeagueName = event.getItem().toString();
        for (CheckboxMenuItem checkboxMenuItem : leagueMenuItems) {
            log.debug(checkboxMenuItem.getLabel());
            checkboxMenuItem.setState(false);
            if (checkboxMenuItem.equals(event.getSource())) {
                checkboxMenuItem.setState(true);
            }
        }
        if (itemResolver != null) {
            itemResolver.refresh(selectedLeagueName);
        }
        pathOfExileAPI.setLeague(selectedLeagueName);
    }

}