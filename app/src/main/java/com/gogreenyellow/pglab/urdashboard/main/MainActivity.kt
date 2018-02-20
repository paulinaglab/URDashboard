package com.gogreenyellow.pglab.urdashboard.main

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.model.QueuedProject
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest
import com.gogreenyellow.pglab.urdashboard.notification.RefreshService
import com.gogreenyellow.pglab.urdashboard.settings.SettingsActivity
import com.gogreenyellow.pglab.urdashboard.util.DateUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sheet_request_json.*
import kotlinx.android.synthetic.main.sheet_request_settings.*
import kotlinx.android.synthetic.main.sheet_slots.*
import kotlinx.android.synthetic.main.sheet_token.*
import kotlinx.android.synthetic.main.srj_request_item.*
import kotlinx.android.synthetic.main.srj_request_item.view.*
import kotlinx.android.synthetic.main.srj_request_project_item.view.*
import kotlinx.android.synthetic.main.srs_project_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), MainContract.View, UpdateTokenDialog.TokenUpdatesListener {

    override lateinit var presenter: MainContract.Presenter

    companion object {
        const val REFRESH_JOB_ID = 100
        const val UDACITY_DASHBOARD_URL = "https://mentor-dashboard.udacity.com/reviews/overview"
        const val UPDATE_TOKEN_DIALOG_TAG = "update_token_dialog"
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        am_swipe_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        am_swipe_refresh_layout.setOnRefreshListener({
            presenter.refreshAll(getToken(), true)
        })

        st_update_button.setOnClickListener { showTokenDialog(true) }
        scheduleJobs()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshAll(getToken(), false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mm_open_udacity_website -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UDACITY_DASHBOARD_URL))
                startActivity(intent)
            }
            R.id.mm_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.mm_notification1 -> displayNotification(
                    R.drawable.ic_notif_default,
                    R.string.n_new_reviews_title,
                    R.string.n_new_reviews_text)
            R.id.mm_notification2 -> displayNotification(
                    R.drawable.ic_notif_price,
                    R.string.n_price_changes_title,
                    R.string.n_price_changes_text)
            R.id.mm_notification3 -> displayNotification(
                    R.drawable.ic_notif_error,
                    R.string.n_incorrect_request_title,
                    R.string.n_incorrect_request_text)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displaySubmissionRequests(submissionRequests: List<SubmissionRequest>) {
        if (submissionRequests.isNotEmpty()) {
            val first = submissionRequests.get(0)
            srs_queue_left_time.text = DateUtil.getTimeLeft(first.closedAt, this)
            srs_queue_end_time.text = DateUtil.getTime(first.closedAt)
        }

        srj_requests_container.removeAllViews()
        for (submissionRequest in submissionRequests) {
            val inflater = LayoutInflater.from(this)
            val root = inflater.inflate(R.layout.srj_request_item, ri_projects_container, false)
            root.ri_index.text = submissionRequests.indexOf(submissionRequest).toString()
            root.ri_id.text = submissionRequest.id.toString()
            root.ri_created.text = formatDate(submissionRequest.createdAt)
            root.ri_close.text = formatDate(submissionRequest.closedAt)
            root.ri_updated.text = formatDate(submissionRequest.updatedAt)
            for (project in submissionRequest.projects) {
                val projectLayout = inflater.inflate(R.layout.srj_request_project_item, root.ri_projects_container, false)
                projectLayout.ri_project_id.text = project.toString()
                root.ri_projects_container.addView(projectLayout)
            }
            srj_requests_container.addView(root)
        }
    }

    override fun displayAssignedSubmissions(assignedSubmissions: List<AssignedSubmission>) {
        for (submission in assignedSubmissions) {
            val slot = ss_slots_overlay.getChildAt(assignedSubmissions.indexOf(submission)) as TextView

            val customShortId = resources.getIdentifier(
                    "project_short_${submission.projectId}", "string", packageName)
            if (customShortId != 0)
                slot.text = resources.getString(customShortId)
            else
                slot.text = submission.projectId.toString()

            slot.background = resources.getDrawable(R.drawable.slot_filled_bg, theme)
            val customColor = getProjectColor(submission.projectId)
            if (customColor != null)
                slot.background.setColorFilter(customColor, PorterDuff.Mode.SRC_IN)
        }
        for (i in assignedSubmissions.size until ss_slots_overlay.childCount) {
            val slot = ss_slots_overlay.getChildAt(i) as TextView
            slot.background = resources.getDrawable(R.drawable.slot_empty_bg)
            slot.text = ""
        }
    }

    override fun displayProjectsQueuedFor(queuedProjects: List<QueuedProject>) {
        val sorted = queuedProjects.sortedWith(compareBy { !it.queuedFor })

        srs_projects_list_container.removeAllViews()
        for (project in sorted) {
            val item = LayoutInflater.from(this).inflate(R.layout.srs_project_item,
                    srs_projects_list_container, false)

            item.pi_id_view.text = project.projectId.toString()
            item.pi_project_name.text = project.projectName
            item.pi_project_price.text = resources.getString(R.string.price_text, project.projectPrice)
            if (project.queuedFor) {
                item.pi_project_queue_state.setImageResource(R.drawable.ic_toggle_switch)

                val customColor = getProjectColor(project.projectId)
                if (customColor != null)
                    item.pi_id_view.background.setColorFilter(
                            customColor, PorterDuff.Mode.SRC_IN)
            } else {
                item.pi_project_queue_state.setImageResource(R.drawable.ic_toggle_switch_off)

                item.pi_id_view.background.setColorFilter(
                        resources.getColor(R.color.default_project_color_off),
                        PorterDuff.Mode.SRC_IN)
            }
            srs_projects_list_container.addView(item)
        }
    }

    override fun hideRefreshing() {
        am_swipe_refresh_layout.isRefreshing = false
    }

    override fun displayTokenData(expiresIn: Long, expirationDate: String) {
        st_token_expires_date_view.text = getString(R.string.st_token_expires_text, expirationDate, expiresIn)
    }

    override fun tokenUpdated() {
        presenter.refreshAll(getToken(), true)
    }

    override fun tokenUpdateFailed(cancelable: Boolean) {
        showTokenDialog(cancelable)
    }

    override fun showTokenDialog(cancelable: Boolean) {
        if (fragmentManager.findFragmentByTag(UPDATE_TOKEN_DIALOG_TAG) == null) {
            val dialog = UpdateTokenDialog.newInstance(cancelable)
            dialog.isCancelable = cancelable
            dialog.show(supportFragmentManager, UPDATE_TOKEN_DIALOG_TAG)
        }
    }

    fun getProjectColor(projectId: Long): Int? {
        val customColorId = resources.getIdentifier("project_color_${projectId}", "color", packageName)
        if (customColorId != 0)
            return resources.getColor(customColorId)
        return null
    }

    fun formatDate(date: String): String {
        return DATE_FORMAT.format(Date(DateUtil.getUdacityTimeInMillis(date)))
    }

    fun getToken(): String? {
        return PreferenceStorage.getInstance(this)?.token
    }

    fun scheduleJobs() {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val componentName = ComponentName(this, RefreshService::class.java)
        val jobInfo = JobInfo.Builder(REFRESH_JOB_ID, componentName)
        jobInfo.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(PreferenceStorage.getInstance(this)!!.requestInterval)
                .setPersisted(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            jobInfo.setRequiresBatteryNotLow(false)
                    .setRequiresStorageNotLow(false)
        }

        scheduler.schedule(jobInfo.build())
    }

    @SuppressLint("NewApi")
    fun displayNotification(smallIconResId: Int,
                            contentTitleResId: Int,
                            contentTextResId: Int) {
        val channelId = "submission_assigned"
        val notificationMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel name", NotificationManager.IMPORTANCE_HIGH)
            notificationMgr.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(smallIconResId)
                .setContentTitle(getString(contentTitleResId))
                .setContentText(getString(contentTextResId))
                .setSound(PreferenceStorage.getInstance(this)!!.newAssignmentSound)
                .setAutoCancel(true)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        notificationMgr.notify(RefreshService.ASSIGNED_REVIEWS_NOTIFICATION_ID, builder.build())
    }
}
