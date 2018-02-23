package com.gogreenyellow.pglab.urdashboard.settings

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.ui.SimpleMenu
import com.gogreenyellow.pglab.urdashboard.util.JobPlanner
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Created by Paulina on 13.02.2018.
 */
class SettingsActivity : AppCompatActivity() {

    companion object {
        const val NEW_ASSIGNMENT_SOUND_REQUEST_CODE = 100
        const val INCORRECT_REQUEST_SOUND_REQUEST_CODE = 101
        const val PRICE_CHANGES_SOUND_REQUEST_CODE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initIntervalValueView()
        as_interval_overlay.setOnClickListener({ openIntervalSimpleMenu() })

        initNewAssignmentSettingState()
        as_new_review_overlay.setOnClickListener({ toggleNotificationNewAssignment() })
        as_new_review_sound_overlay.setOnClickListener({
            openNotificationSoundPicker(
                    PreferenceStorage.getInstance(this)?.newAssignmentSound,
                    NEW_ASSIGNMENT_SOUND_REQUEST_CODE)
        })

        initIncorrectRequestSettingState()
        as_incorrect_request_overlay.setOnClickListener({ toggleNotificationRequestIncorrect() })
        as_incorrect_request_sound_overlay.setOnClickListener({
            openNotificationSoundPicker(PreferenceStorage.getInstance(this)?.requestIncorrectSound,
                    INCORRECT_REQUEST_SOUND_REQUEST_CODE)
        })

        initPriceChangesSettingState()
        as_price_changes_overlay.setOnClickListener({ toggleNotificationPriceChanges() })
        as_price_changes_sound_overlay.setOnClickListener({
            openNotificationSoundPicker(
                    PreferenceStorage.getInstance(this)?.priceChangesSound,
                    PRICE_CHANGES_SOUND_REQUEST_CODE)
        })
    }

    private fun initNewAssignmentSettingState() {
        val psInstance = PreferenceStorage.getInstance(this)
        as_new_review_switch.isChecked = psInstance!!.isNotifyNewAssignment
        initNotificationSoundName(
                as_new_review_sound_name,
                psInstance.newAssignmentSound)
    }

    private fun initIncorrectRequestSettingState() {
        val psInstance = PreferenceStorage.getInstance(this)
        as_incorrect_request_switch.isChecked = psInstance!!.isNotifyIncorrectRequest
        initNotificationSoundName(
                as_incorrect_request_sound_name,
                psInstance.requestIncorrectSound)
    }

    private fun initPriceChangesSettingState() {
        val psInstance = PreferenceStorage.getInstance(this)
        as_price_changes_switch.isChecked = psInstance!!.isNotifyPriceChanges
        initNotificationSoundName(
                as_price_changes_sound_name,
                psInstance.priceChangesSound)
    }

    private fun initNotificationSoundName(soundNameView: TextView, ringtoneUri: Uri?) {
        val ringtone = RingtoneManager.getRingtone(this, ringtoneUri)
        soundNameView.text = ringtone.getTitle(this)
    }

    fun initIntervalValueView() {
        val labelArray = resources.getStringArray(R.array.as_interval_options_array)
        val millisArray = resources.getIntArray(R.array.as_interval_millis)

        val prefValue = PreferenceStorage.getInstance(this)?.requestInterval

        as_interval_value_view.text = labelArray.get(millisArray.indexOf(prefValue?.toInt()!!))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    private fun openIntervalSimpleMenu() {
        val overlay = as_interval_overlay
        val loc = IntArray(2).apply { overlay.getLocationOnScreen(this) }

        val anchorBounds = Rect(
                loc[0] + overlay.paddingLeft,
                loc[1],
                loc[0] + overlay.width - overlay.paddingLeft,
                loc[1] + overlay.height)


        var millisArray = resources.getIntArray(R.array.as_interval_millis)
        var labelsArray = resources.getStringArray(R.array.as_interval_options_array)
        val currentValue = PreferenceStorage.getInstance(this)?.requestInterval


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            millisArray = millisArray.copyOfRange(1, millisArray.size)
            labelsArray = labelsArray.copyOfRange(1, labelsArray.size)
        }

        val simpleMenu = SimpleMenu(this,
                labelsArray,
                millisArray.indexOf(currentValue?.toInt()!!))

        simpleMenu.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            PreferenceStorage.getInstance(this@SettingsActivity)?.requestInterval = millisArray[position].toLong()
            initIntervalValueView()
            simpleMenu.dismiss()
        })

        simpleMenu.show(overlay, anchorBounds)
    }

    private fun openNotificationSoundPicker(currentTone: Uri?, requestCode: Int) {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.select_notification_sound))
                .putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)

        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.extras?.getParcelable<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            when (requestCode) {
                NEW_ASSIGNMENT_SOUND_REQUEST_CODE -> {
                    uri?.let { PreferenceStorage.getInstance(this)?.newAssignmentSound = uri }
                    initNotificationSoundName(as_new_review_sound_name, uri)
                }
                INCORRECT_REQUEST_SOUND_REQUEST_CODE -> {
                    uri?.let { PreferenceStorage.getInstance(this)?.requestIncorrectSound = uri }
                    initNotificationSoundName(as_incorrect_request_sound_name, uri)
                }
                PRICE_CHANGES_SOUND_REQUEST_CODE -> {
                    uri?.let { PreferenceStorage.getInstance(this)?.priceChangesSound = uri }
                    initNotificationSoundName(as_price_changes_sound_name, uri)
                }

            }

        }
    }

    private fun toggleNotificationNewAssignment() {
        val psInstance = PreferenceStorage.getInstance(this)
        psInstance?.isNotifyNewAssignment = !(psInstance?.isNotifyNewAssignment!!)
        initNewAssignmentSettingState()
        JobPlanner.scheduleJobs(this)
    }

    private fun toggleNotificationRequestIncorrect() {
        val psInstance = PreferenceStorage.getInstance(this)
        psInstance?.isNotifyIncorrectRequest = !(psInstance?.isNotifyIncorrectRequest!!)
        initIncorrectRequestSettingState()
        JobPlanner.scheduleJobs(this)
    }

    private fun toggleNotificationPriceChanges() {
        val psInstance = PreferenceStorage.getInstance(this)
        psInstance?.isNotifyPriceChanges = !(psInstance?.isNotifyPriceChanges!!)
        initPriceChangesSettingState()
        JobPlanner.scheduleJobs(this)
    }
}