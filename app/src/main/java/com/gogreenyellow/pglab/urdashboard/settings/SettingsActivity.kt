package com.gogreenyellow.pglab.urdashboard.settings

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Created by Paulina on 13.02.2018.
 */
class SettingsActivity : AppCompatActivity() {

    companion object {
        val NOTIFICATION_SOUND_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        as_notification_sound_overlay.setOnClickListener({ openNotificationSoundPicker() })
        as_new_review_overlay.setOnClickListener({ toggleNotificationNewAssignment() })
        as_incorrect_request_overlay.setOnClickListener({ toggleNotificationRequestIncorrect() })
        as_price_changes_overlay.setOnClickListener({ toggleNotificationPriceChanges() })

        initNotificationSoundName()
        initNewAssignmentSettingState()
        initIncorrectRequestSettingState()
        initPriceChangesSettingState()
    }

    fun initNotificationSoundName() {
        val currentTone = PreferenceStorage.getInstance(this)?.notificationSound
        val ringtone = RingtoneManager.getRingtone(this, currentTone)
        as_notification_sound_name.text = ringtone.getTitle(this)
    }

    fun initNewAssignmentSettingState() {
        as_new_review_switch.isChecked = PreferenceStorage.getInstance(this)!!.isNotifyNewAssignment
    }

    fun initIncorrectRequestSettingState() {
        as_incorrect_request_switch.isChecked = PreferenceStorage.getInstance(this)!!.isNotifyIncorrectRequest
    }

    fun initPriceChangesSettingState() {
        as_price_changes_switch.isChecked = PreferenceStorage.getInstance(this)!!.isNotifyPriceChanges
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    fun openNotificationSoundPicker() {
        val currentTone = PreferenceStorage.getInstance(this)?.notificationSound
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.select_notification_sound))
                .putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)

        startActivityForResult(intent, NOTIFICATION_SOUND_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.extras?.getParcelable<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            uri?.let { PreferenceStorage.getInstance(this)?.saveNotificationSound(it) }
            initNotificationSoundName()
        }
    }

    fun toggleNotificationNewAssignment() {
        val psInstance = PreferenceStorage.getInstance(this)
        psInstance?.isNotifyNewAssignment = !(psInstance?.isNotifyNewAssignment!!)
        initNewAssignmentSettingState()
    }

    fun toggleNotificationRequestIncorrect() {
        val psInstance = PreferenceStorage.getInstance(this)
        psInstance?.isNotifyIncorrectRequest = !(psInstance?.isNotifyIncorrectRequest!!)
        initIncorrectRequestSettingState()
    }

    fun toggleNotificationPriceChanges() {
        val psInstance = PreferenceStorage.getInstance(this)
        psInstance?.isNotifyPriceChanges = !(psInstance?.isNotifyPriceChanges!!)
        initPriceChangesSettingState()
    }
}