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

        private val NOTIFY_NEW_ASSIGNMENT = "notify new assignment"
        private val NOTIFICATION_SOUND_NEW_ASSIGNMENT = "notification sound new assignment"
        private val NOTIFY_REQUEST_INCORRECT = "notify request incorrect"
        private val NOTIFICATION_SOUND_REQUEST_INCORRECT = "notification request incorrect"
        private val NOTIFY_PRICE_CHANGES = "notify price changes"
        private val NOTIFICATION_SOUND_PRICE_CHANGES = "notification sound price changes"
        private val REQUEST_INTERVAL = "interval"
        private val TOKEN = "token"
    }

    var isNotifyNewAssignment: Boolean
        get() {
            return sharedPreferences.getBoolean(NOTIFY_NEW_ASSIGNMENT, true)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putBoolean(NOTIFY_NEW_ASSIGNMENT, value).commit()
        }

    var newAssignmentSound: Uri
        get() {
            val defaultSystemNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
            val uri = sharedPreferences.getString(NOTIFICATION_SOUND_NEW_ASSIGNMENT, defaultSystemNotification)
            return Uri.parse(uri)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putString(NOTIFICATION_SOUND_NEW_ASSIGNMENT, value.toString()).commit()
        }

    var isNotifyIncorrectRequest: Boolean
        get() {
            return sharedPreferences.getBoolean(NOTIFY_REQUEST_INCORRECT, false)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putBoolean(NOTIFY_REQUEST_INCORRECT, value).commit()
        }

    var requestIncorrectSound: Uri
        get() {
            val defaultSystemNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
            val uri = sharedPreferences.getString(NOTIFICATION_SOUND_REQUEST_INCORRECT, defaultSystemNotification)
            return Uri.parse(uri)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putString(NOTIFICATION_SOUND_REQUEST_INCORRECT, value.toString()).commit()
        }

    var isNotifyPriceChanges: Boolean
        get() {
            return sharedPreferences.getBoolean(NOTIFY_PRICE_CHANGES, true)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putBoolean(NOTIFY_PRICE_CHANGES, value).commit()
        }

    var priceChangesSound: Uri
        get() {
            val defaultSystemNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
            val uri = sharedPreferences.getString(NOTIFICATION_SOUND_PRICE_CHANGES, defaultSystemNotification)
            return Uri.parse(uri)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putString(NOTIFICATION_SOUND_PRICE_CHANGES, value.toString()).commit()
        }

    var token: String?
        get() {
            return sharedPreferences.getString(TOKEN, null)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putString(TOKEN, value).commit()
        }

    var requestInterval: Long
        get() {
            return sharedPreferences.getLong(REQUEST_INTERVAL, 900000)
        }
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putLong(REQUEST_INTERVAL, value).commit()
        }

}