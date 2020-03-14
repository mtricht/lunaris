package dev.tricht.lunaris

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI
import dev.tricht.lunaris.com.pathofexile.itemtransformer.ItemTransformer
import dev.tricht.lunaris.com.pathofexile.middleware.ModValueRangeMiddleware
import dev.tricht.lunaris.com.pathofexile.middleware.PseudoModsMiddleware
import dev.tricht.lunaris.com.pathofexile.middleware.TradeMiddleware
import dev.tricht.lunaris.info.poeprices.PoePricesAPI
import dev.tricht.lunaris.item.ItemGrabber
import dev.tricht.lunaris.listeners.*
import dev.tricht.lunaris.ninja.poe.ItemResolver
import dev.tricht.lunaris.util.ErrorUtil
import dev.tricht.lunaris.util.Properties
import dev.tricht.lunaris.util.SystemTray
import javafx.application.Platform
import org.jnativehook.GlobalScreen
import org.slf4j.LoggerFactory

import javax.swing.*
import java.awt.*
import java.util.ArrayList
import java.util.logging.Level
import java.util.logging.Logger

class Lunaris private constructor() {

    init {
        var pathOfExileAPI: PathOfExileAPI? = null
        try {
            pathOfExileAPI = PathOfExileAPI()
        } catch (e: Exception) {
            log.error("Failed talking to pathofexile.com", e)
            ErrorUtil.showErrorDialogAndExit("Couldn't talk with pathofexile.com, perhaps down for maintenance?")
        }

        try {
            Properties.load()
        } catch (e: Exception) {
            ErrorUtil.showErrorDialogAndExit("Unable to create setup properties file for settings, try running as administrator.")
        }

        SystemTray.setup()
        val poePricesAPI = PoePricesAPI()

        setupItemTransformerMiddleware()
        Properties.addPropertyListener("trade_search.(.*)") {
            log.debug("Refreshing item transformer middleware")
            setupItemTransformerMiddleware()
        }

        var robot: Robot? = null
        try {
            robot = Robot()
        } catch (e: AWTException) {
            log.error("Failed to initialize ", e)
            ErrorUtil.showErrorDialogAndExit("Failed to initialize Lunaris.")
        }

        val itemResolver = ItemResolver()
        val itemGrabber = ItemGrabber(itemResolver)

        Properties.addPropertyListener(Properties.LEAGUE) {
            itemResolver.refresh()
            SystemTray.selectLeague(Properties.getProperty(Properties.LEAGUE))
        }

        Properties.addPropertyListener(Properties.POESESSID) {
            pathOfExileAPI?.sessionId =  Properties.getProperty(Properties.POESESSID)
        }

        ListenerStack().startListeners(itemGrabber, robot, pathOfExileAPI, poePricesAPI)

        // For some reason the JavaFX thread will completely stop after closing
        // the first tooltip. Setting this will prevent that from happening.
        Platform.setImplicitExit(false)
        log.debug("Ready!")
    }

    private fun setupItemTransformerMiddleware() {
        val tradeMiddlewareArrayList = ArrayList<TradeMiddleware>()
        if (Properties.getProperty("trade_search.pseudo_mods", "1") == "1") {
            tradeMiddlewareArrayList.add(PseudoModsMiddleware())
        }
        if (Properties.getProperty("trade_search.range_search", "1") == "1") {
            tradeMiddlewareArrayList.add(
                    ModValueRangeMiddleware(
                            Integer.parseInt(Properties.getProperty("trade_search.range_search_percentage", "20")),
                            Properties.getProperty("trade_search.range_search_only_min", "1") != "1"
                    )
            )
        }
        ItemTransformer.setMiddleware(tradeMiddlewareArrayList)
    }

    companion object {
        private val log = LoggerFactory.getLogger(Lunaris::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                // 0.4.0 added FXML from JavaFX which requires the scripting engine jmod
                Class.forName("javax.script.ScriptEngine")
            } catch (e: ClassNotFoundException) {
                ErrorUtil.showErrorDialogAndExit("Please manually download the latest release. You're running a version that can not be auto-updated.")
            }

            // Disable logging from jnativehook (logs every keystroke or mouse movement)
            val logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
            logger.level = Level.WARNING
            logger.useParentHandlers = false
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (e: Exception) {
                log.warn("Could not set System Look and Feel, falling back to default.")
            }

            Lunaris()
        }
    }
}