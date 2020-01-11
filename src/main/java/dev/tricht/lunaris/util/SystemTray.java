package dev.tricht.lunaris.util;

import dev.tricht.lunaris.Lunaris;
import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.net.URL;
import java.util.ArrayList;

@Slf4j
public class SystemTray {

    private static ArrayList<CheckboxMenuItem> leagueMenuItems = new ArrayList<>();
    private static String selectedLeagueName;
    private static LeagueCallback leagueCallback;

    public static String create(PathOfExileAPI pathOfExileAPI, LeagueCallback leagueCallback) {
        if (!java.awt.SystemTray.isSupported()) {
            ErrorUtil.showErrorDialogAndExit("Platform is currently not supported.");
            log.error("SystemTray is not supported");
            System.exit(1);
        }
        SystemTray.leagueCallback = leagueCallback;
        final PopupMenu popup = new PopupMenu("Lunaris");
        final TrayIcon trayIcon = new TrayIcon(getIcon());
        trayIcon.setImageAutoSize(true);
        final java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();

        MenuItem POESESSID = new MenuItem("POESESSID");
        POESESSID.addActionListener(e -> {
            String poesessid = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter the POESESSID cookie to prevent rate limits",
                    "Lunaris POESESSID",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    pathOfExileAPI.getSessionId()
            );
            if (poesessid == null) {
                return;
            }
            pathOfExileAPI.setSessionId(poesessid);
            PropertiesManager.writeProperty(PropertiesManager.POESESSID, poesessid);
        });

        Menu leagueMenu = new Menu("League");
        int count = 0;
        CheckboxMenuItem leagueToSelect = null;
        for (String leagueName : pathOfExileAPI.getTradeLeagues()) {
            CheckboxMenuItem leagueMenuItem = new CheckboxMenuItem(leagueName);
            leagueMenuItems.add(leagueMenuItem);
            leagueMenu.add(leagueMenuItem);
            leagueMenuItem.addItemListener(SystemTray::changeLeague);
            if ((PropertiesManager.containsKey(PropertiesManager.LEAGUE)
                    && PropertiesManager.getProperty(PropertiesManager.LEAGUE).equals(leagueName)) || (count == 2 && leagueToSelect == null)) {
                leagueToSelect = leagueMenuItem;
            }
            count++;
        }
        changeLeague(new ItemEvent(leagueToSelect, 0, leagueToSelect.getLabel(), ItemEvent.SELECTED));

        MenuItem exitItem = new MenuItem("Exit");

        popup.add(POESESSID);
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

        return selectedLeagueName;
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

    private static void changeLeague(ItemEvent event) {
        String newLeagueName = event.getItem().toString();
        if (selectedLeagueName != null && selectedLeagueName.equals(newLeagueName)) {
            return;
        }
        selectedLeagueName = event.getItem().toString();
        for (CheckboxMenuItem checkboxMenuItem : leagueMenuItems) {
            checkboxMenuItem.setState(false);
            if (checkboxMenuItem.equals(event.getSource())) {
                checkboxMenuItem.setState(true);
            }
        }
        PropertiesManager.writeProperty(PropertiesManager.LEAGUE, selectedLeagueName);
        leagueCallback.changeLeague(selectedLeagueName);
    }

    public interface LeagueCallback {
        void changeLeague(String leagueName);
    }

}
