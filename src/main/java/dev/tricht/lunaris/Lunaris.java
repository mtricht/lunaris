package dev.tricht.lunaris;

import dev.tricht.lunaris.com.pathofexile.Leagues;
import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.listeners.*;
import dev.tricht.lunaris.ninja.poe.ItemResolver;
import dev.tricht.lunaris.util.ErrorUtil;
import dev.tricht.lunaris.util.PropertiesManager;
import dev.tricht.lunaris.util.SystemTray;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.GlobalScreen;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class Lunaris {

    private PathOfExileAPI pathOfExileAPI;

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        new Lunaris();
    }

    private Lunaris() {
        PropertiesManager.load();
        try {
            pathOfExileAPI = new PathOfExileAPI();
            Leagues.load(pathOfExileAPI);
        } catch (Exception e) {
            log.error("Failed talking to pathofexile.com", e);
            ErrorUtil.showErrorDialogAndExit("Couldn't talk with pathofexile.com, perhaps down for maintenance?");
        }
        String leagueName = SystemTray.create(pathOfExileAPI);
        pathOfExileAPI.setLeague(leagueName);

        try {
            Robot robot = new Robot();
            ItemResolver itemResolver = new ItemResolver(leagueName);
            ItemGrabber itemGrabber = new ItemGrabber(robot, itemResolver);

            PropertiesManager.addPropertyListener(PropertiesManager.LEAGUE, () -> {
                log.debug("New league newLeagueName, refreshing API, system tray and item resolver");
                String newLeagueName = PropertiesManager.getProperty(PropertiesManager.LEAGUE);
                itemResolver.refresh(newLeagueName);
                pathOfExileAPI.setLeague(newLeagueName);
                SystemTray.selectLeague(newLeagueName);
            });

            PropertiesManager.addPropertyListener(PropertiesManager.POESESSID, () -> {
                String poesessid = PropertiesManager.getProperty(PropertiesManager.POESESSID);
                pathOfExileAPI.setSessionId(poesessid);
            });

            new ListenerStack().startListeners(itemGrabber, robot, pathOfExileAPI);
        } catch (IOException | AWTException e) {
            log.error("Failed to initialize ", e);
            return;
        }
        // For some reason the JavaFX thread will completely stop after closing
        // the first tooltip. Setting this will prevent that from happening.
        Platform.setImplicitExit(false);
        log.debug("Ready!");
    }
}