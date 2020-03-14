package dev.tricht.lunaris;

import dev.tricht.lunaris.com.pathofexile.Leagues;
import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.com.pathofexile.itemtransformer.ItemTransformer;
import dev.tricht.lunaris.com.pathofexile.middleware.ModValueRangeMiddleware;
import dev.tricht.lunaris.com.pathofexile.middleware.PseudoModsMiddleware;
import dev.tricht.lunaris.com.pathofexile.middleware.TradeMiddleware;
import dev.tricht.lunaris.info.poeprices.PoePricesAPI;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.listeners.*;
import dev.tricht.lunaris.ninja.poe.ItemResolver;
import dev.tricht.lunaris.util.ErrorUtil;
import dev.tricht.lunaris.util.PropertiesManager;
import dev.tricht.lunaris.util.SystemTray;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.GlobalScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class Lunaris {

    private PathOfExileAPI pathOfExileAPI;

    public static void main(String[] args) {
        try {
            // 0.4.0 added FXML from JavaFX which requires the scripting engine jmod
            Class.forName("javax.script.ScriptEngine");
        } catch (ClassNotFoundException e) {
            ErrorUtil.showErrorDialogAndExit("Please manually download the latest release. You're running a version that can not be auto-updated.");
        }
        // Disable logging from jnativehook (logs every keystroke or mouse movement)
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            logger.warning("Could not set System Look and Feel, falling back to default.");
        }
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
        PoePricesAPI poePricesAPI = new PoePricesAPI(leagueName);

        setupItemTransformerMiddleware();
        PropertiesManager.addPropertyListener("trade_search.(.*)", () -> {
            log.debug("Refreshing item transformer middleware");
            setupItemTransformerMiddleware();
        });

        try {
            Robot robot = new Robot();
            ItemResolver itemResolver = new ItemResolver(leagueName);
            ItemGrabber itemGrabber = new ItemGrabber(itemResolver);

            PropertiesManager.addPropertyListener(PropertiesManager.LEAGUE, () -> {
                log.debug("New league newLeagueName, refreshing API, system tray and item resolver");
                String newLeagueName = PropertiesManager.getProperty(PropertiesManager.LEAGUE);
                itemResolver.refresh(newLeagueName);
                pathOfExileAPI.setLeague(newLeagueName);
                poePricesAPI.setLeagueName(newLeagueName);
                SystemTray.selectLeague(newLeagueName);
            });

            PropertiesManager.addPropertyListener(PropertiesManager.POESESSID, () -> {
                String poesessid = PropertiesManager.getProperty(PropertiesManager.POESESSID);
                pathOfExileAPI.setSessionId(poesessid);
            });

            new ListenerStack().startListeners(itemGrabber, robot, pathOfExileAPI, poePricesAPI);
        } catch (AWTException e) {
            log.error("Failed to initialize ", e);
            return;
        }
        // For some reason the JavaFX thread will completely stop after closing
        // the first tooltip. Setting this will prevent that from happening.
        Platform.setImplicitExit(false);
        log.debug("Ready!");
    }

    private void setupItemTransformerMiddleware() {
        ArrayList<TradeMiddleware> tradeMiddlewareArrayList = new ArrayList<>();
        if (PropertiesManager.getProperty("trade_search.pseudo_mods", "1").equals("1")) {
            tradeMiddlewareArrayList.add(new PseudoModsMiddleware());
        }
        if (PropertiesManager.getProperty("trade_search.range_search", "1").equals("1")) {
            tradeMiddlewareArrayList.add(
                    new ModValueRangeMiddleware(
                            Integer.parseInt(PropertiesManager.getProperty("trade_search.range_search_percentage", "20")),
                            !PropertiesManager.getProperty("trade_search.range_search_only_min", "1").equals("1")
                    )
            );
        }
        ItemTransformer.setMiddleware(tradeMiddlewareArrayList);
    }
}