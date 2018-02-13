package com.gogreenyellow.pglab.urdashboard.util

import android.content.Context
import com.gogreenyellow.pglab.urdashboard.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Paulina on 2018-02-13.
 */
class DateUtil {

    companion object {

        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z")
        val TIME_FORMAT = SimpleDateFormat("HH:mm")

        fun getTime(date: String): String {
            val d = DATE_FORMAT.parse(date + "+0000")
            val calendar = Calendar.getInstance()
            calendar.time = d
            return TIME_FORMAT.format(d)
        }

        fun getTimeLeft(date: String, context: Context): String {
            val d = DATE_FORMAT.parse(date + "+0000")
            val timeFuture = d.time
            val timeNow = Calendar.getInstance().timeInMillis
            var minutes = (timeFuture - timeNow) / 60000
            val hours: Int = (minutes / 60).toInt()
            minutes %= 60
            return context.getString(R.string.srs_queue_last_text, hours, minutes).toString()
        }

        fun getUdacityTimeInMillis(date: String): Long {
            return DATE_FORMAT.parse(date + "+0000").time
        }

        fun minutesLeft(date: String): Long {
            return (getUdacityTimeInMillis(date) - System.currentTimeMillis()) / 60000
        }
    }
}