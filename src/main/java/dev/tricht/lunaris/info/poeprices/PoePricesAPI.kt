package dev.tricht.lunaris.info.poeprices

import com.fasterxml.jackson.databind.ObjectMapper
import dev.tricht.lunaris.Lunaris
import dev.tricht.lunaris.item.Item
import dev.tricht.lunaris.util.Properties
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

import java.io.IOException
import java.util.Base64

class PoePricesAPI() {

    fun getItem(item: Item, callback: Callback) {
        if (Properties.getProperty("trade_search.poeprices") == "0") {
            return
        }
        val itemEncoded = Base64.getEncoder().encodeToString(item.clipboardText.toByteArray())
        val request = Request.Builder().url(
                String.format("https://www.poeprices.info/api?l=%s&i=%s", Properties.league, itemEncoded)
        ).build()
        //try {
            client.newCall(request).enqueue(callback)
//            objectMapper.readValue(response.body!!.string(), ItemPricePrediction::class.java)
//        } catch (e: IOException) {
//            log.error("Failed to call poeprices.info", e)
//            null
//        }
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(Lunaris::class.java)
        private var client = OkHttpClient()
        private var objectMapper = ObjectMapper()
    }
}
