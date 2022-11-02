package com.yaossg.clock.data

import org.json.JSONObject
import java.net.URL
import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

data class RemoteClock(val current_time: String, val timelist: Array<String>) {
    companion object {
        fun timeOf(text: String) = ZonedDateTime.parse(text).toLocalTime().toString().substringBefore('.')
        fun resolve(): RemoteClock {
            val url = URL("https://632a6f811090510116c05b32.mockapi.io/timelist")
            val json = JSONObject(url.openStream().reader().use { it.readText() })
            val jsonArray = json.getJSONArray("timelist")
            return RemoteClock(timeOf(json.getString("current_time")),
                Array(jsonArray.length()) { timeOf(jsonArray.getString(it)) })
        }

        val now = AtomicReference<RemoteClock>()

        val updater = thread(isDaemon = true, start = false) {
            while (true) {
                now.set(resolve())
                Thread.sleep(1000)
            }
        }
    }

}