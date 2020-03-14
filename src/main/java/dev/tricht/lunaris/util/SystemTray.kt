package dev.tricht.lunaris.util

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI
import dev.tricht.lunaris.settings.SettingsFrame
import dev.tricht.lunaris.settings.SettingsWindow
import javafx.application.Platform
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.slf4j.LoggerFactory
import java.awt.*
import java.awt.event.ItemEvent
import java.util.*
import javax.swing.ImageIcon
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

object SystemTray {
    private val leagueMenuItems = ArrayList<CheckboxMenuItem>()
    private val log = LoggerFactory.getLogger(SystemTray::class.java)

    fun setup() {
        if (!java.awt.SystemTray.isSupported()) {
            return;
        }
        val popup = PopupMenu("Lunaris")
        val trayIcon = TrayIcon(icon)
        trayIcon.isImageAutoSize = true
        val tray = java.awt.SystemTray.getSystemTray()
        val leagueMenu = Menu("League")
        var leagueToSelect: CheckboxMenuItem? = null
        for (leagueName in PathOfExileAPI.getTradeLeagues()) {
            val leagueMenuItem = CheckboxMenuItem(leagueName)
            leagueMenuItems.add(leagueMenuItem)
            leagueMenu.add(leagueMenuItem)
            leagueMenuItem.addItemListener(this::changeLeagueEventHandler)
            if (leagueName == Properties.league) {
                leagueToSelect = leagueMenuItem
            }
        }
        changeLeagueEventHandler(ItemEvent(leagueToSelect, 0, leagueToSelect!!.label, ItemEvent.SELECTED))
        val settings = MenuItem("Settings...")
        settings.addActionListener {
            SwingUtilities.invokeLater {
                val window = SettingsWindow()
                val frame = SettingsFrame()
                window.add(frame)
                Platform.runLater {
                    frame.init()
                    window.showSettings(frame.layoutBounds)
                }
            }
        }
        val exitItem = MenuItem("Exit")
        popup.add(leagueMenu)
        popup.addSeparator()
        popup.add(settings)
        popup.addSeparator()
        popup.add(exitItem)
        trayIcon.popupMenu = popup
        try {
            tray.add(trayIcon)
        } catch (e: AWTException) {
            log.error("TrayIcon could not be added.")
        }
        exitItem.addActionListener {
            try {
                GlobalScreen.unregisterNativeHook()
            } catch (ex: NativeHookException) {
                log.error("Failed to unregister native hook", ex)
            }
            exitProcess(0)
        }
    }

    private val icon: Image?
        get() {
            val imageURL = SystemTray::class.java.classLoader.getResource("icon.png")
            return if (imageURL == null) {
                log.error("icon.png missing")
                null
            } else {
                ImageIcon(imageURL, "Lunaris").image
            }
        }

    private fun changeLeagueEventHandler(event: ItemEvent) {
        val newLeagueName = event.item.toString()
        if (Properties.league == newLeagueName) {
            return
        }
        Properties.league = event.item.toString()
        selectLeague(Properties.league)
    }

    fun selectLeague(league: String?) {
        for (checkboxMenuItem in leagueMenuItems) {
            checkboxMenuItem.state = false
            if (checkboxMenuItem.label == league) {
                checkboxMenuItem.state = true
            }
        }
    }
}