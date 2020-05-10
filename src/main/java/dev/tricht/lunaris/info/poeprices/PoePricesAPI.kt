package dev.tricht.lunaris.info.poeprices

import dev.tricht.lunaris.item.Item
import dev.tricht.lunaris.util.Properties
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

import java.util.Base64

class PoePricesAPI() {

    fun getItem(item: Item, callback: Callback) {
        val itemEncoded = Base64.getEncoder().encodeToString(item.clipboardText.toByteArray())
        val request = Request.Builder().url(
                String.format("https://www.poeprices.info/api?l=%s&i=%s", Properties.league, itemEncoded)
        ).build()
        client.newCall(request).enqueue(callback)
    }

    companion object {
        private var client = OkHttpClient()
    }
}
