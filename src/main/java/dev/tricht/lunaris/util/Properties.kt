package dev.tricht.lunaris.util

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI

import java.io.*
import java.util.HashMap

object Properties {

    private var file: File = DirectoryManager.getFile("lunaris.properties")
    private val properties = java.util.Properties()

    const val LEAGUE = "LEAGUE"
    const val CHARACTER_NAME = "CHARACTER_NAME"
    const val POESESSID = "POESESSID"

    @JvmStatic
    var league: String? = null
        set(value) {
            writeProperty(LEAGUE, value)
            field = value
        }

    private val propertyListeners = HashMap<String, (String?) -> Unit>()

    private val defaults: Map<String, String> = mapOf(
            // Keybinds
            Pair("keybinds.price_check", "Alt+D"),
            Pair("keybinds.search_trade", "Alt+Q"),
            Pair("keybinds.item_info", "Alt+A"),
            Pair("keybinds.hideout", "F5"),
            Pair("keybinds.wiki", "Alt+W"),
            Pair("keybinds.kick", "F4"),
            Pair("keybinds.invite_last_whisper", "F6"),
            Pair("keybinds.enable_stash_scroll", "Alt+D"),

            // Trade settings
            Pair("trade_search.pseudo_mods", "1"),
            Pair("trade_search.range_search", "1"),
            Pair("trade_search.range_search_percentage", "20"),
            Pair("trade_search.range_search_only_min", "1"),
            Pair("trade_search.poeprices", "1"),

            // Map mods
            Pair("map_mod_warnings.ele_refl", "1"),
            Pair("map_mod_warnings.phys_refl", "1"),
            Pair("map_mod_warnings.no_leech", "1"),
            Pair("map_mod_warnings.no_regen", "1"),
            Pair("map_mod_warnings.multi_dmg", "1"),
            Pair("map_mod_warnings.tmp_chains", "1"),
            Pair("map_mod_warnings.low_recovery", "1"),
            Pair("map_mod_warnings.avoid_elemental_ailments", "1"),
            Pair("map_mod_warnings.avoid_poison_blind_and_bleed", "1")
    )

    fun load() {
        file.createNewFile()
        properties.load(FileInputStream(file))
        defaults.filter {(key, _) -> properties.containsKey(key)}
                .forEach {(key, value) -> properties.setProperty(key, value)}
        league = if (properties.containsKey(LEAGUE)) {
            getProperty(LEAGUE)
        } else {
            val tradeLeagues = PathOfExileAPI.getTradeLeagues()
            val defaultLeagueIndex = if (tradeLeagues.size == 4) 2 else 0
            tradeLeagues[defaultLeagueIndex]
        }
    }

    fun getAllPropertiesMatching(regex: String): HashMap<String, String?> {
        return properties.filter {(key, _) -> (key as String).matches(regex.toRegex())}
                .toMap() as HashMap<String, String?>
    }

    fun getProperty(key: String): String? {
        return properties.getProperty(key)
    }

    fun getProperty(key: String, defaultVal: String): String? {
        return if (properties.containsKey(key)) {
            getProperty(key)
        } else defaultVal
    }

    fun writeProperty(key: String, value: String?) {
        properties.setProperty(key, value)
        try {
            properties.store(FileOutputStream(file), null)
        } catch (ex: IOException) {
            ErrorUtil.showErrorDialogAndExit("Unable to save lunaris.properties")
        }

        for ((key1, value1) in propertyListeners) {
            if (key.matches(key1.toRegex())) {
                value1.invoke(value)
            }
        }
    }

    fun addPropertyListener(propMask: String, listener: (String?) -> Unit) {
        propertyListeners[propMask] = listener
    }

}
