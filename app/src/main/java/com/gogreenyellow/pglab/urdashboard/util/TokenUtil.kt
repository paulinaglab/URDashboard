package com.gogreenyellow.pglab.urdashboard.util

import android.util.Base64
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


/**
 * Created by Paulina on 2018-02-18.
 */
object TokenUtil {

    private var dateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun getTokenExpirationDate(token: String): String {
        return dateFormat.format(getExpirationCalendar(token).time)
    }

    fun getTokenExpiresIn(token: String): Long {
        val expirationCalendar = getExpirationCalendar(token)
        val calendarCurrent = Calendar.getInstance()

        val then = expirationCalendar.timeInMillis
        val now = calendarCurrent.timeInMillis

        return (then - now) / (1000 * 60 * 60 * 24)
    }

    private fun getExpirationCalendar(token: String): Calendar {
        val jsonData = JSONObject(String(Base64.decode(token.split('.')[1], 0)))
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = jsonData.getLong("exp") * 1000
        return calendar
    }
}