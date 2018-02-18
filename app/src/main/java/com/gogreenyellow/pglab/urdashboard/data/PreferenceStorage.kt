package com.gogreenyellow.pglab.urdashboard.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.preference.PreferenceManager


/**
 * Created by Paulina on 14.02.2018.
 */
class PreferenceStorage private constructor(context: Context) {

    private var sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private var INSTANCE: PreferenceStorage? = null

        fun getInstance(context: Context): PreferenceStorage? {
            if (INSTANCE == null) {
                INSTANCE = PreferenceStorage(context)
            }
            return INSTANCE
        }

        private val NOTIFICATION_SOUND = "notification sound"
        private val NOTIFY_NEW_ASSIGNMENT = "notify new assignment"
        private val NOTIFY_REQUEST_INCORRECT = "notify request incorrect"
        private val NOTIFY_PRICE_CHANGES = "notify price changes"
        private val TOKEN = "token"
    }


    @SuppressLint("ApplySharedPref")
    fun saveNotificationSound(notificationSound: Uri) {
        sharedPreferences.edit().putString(NOTIFICATION_SOUND, notificationSound.toString()).commit()
    }

    val notificationSound: Uri
        get() {
            val defaultSystemNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
            val uri = sharedPreferences.getString(NOTIFICATION_SOUND, defaultSystemNotification)
            return Uri.parse(uri)
        }

    var isNotifyNewAssignment: Boolean
        get() {
            return sharedPreferences.getBoolean(NOTIFY_NEW_ASSIGNMENT, true)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putBoolean(NOTIFY_NEW_ASSIGNMENT, value).commit()
        }

    var isNotifyIncorrectRequest: Boolean
        get() {
            return sharedPreferences.getBoolean(NOTIFY_REQUEST_INCORRECT, false)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putBoolean(NOTIFY_REQUEST_INCORRECT, value).commit()
        }

    var isNotifyPriceChanges: Boolean
        get() {
            return sharedPreferences.getBoolean(NOTIFY_PRICE_CHANGES, true)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putBoolean(NOTIFY_PRICE_CHANGES, value).commit()
        }

    var token: String?
        get() {
            return sharedPreferences.getString(TOKEN, null)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putString(TOKEN, value).commit()
        }

}