package com.gogreenyellow.pglab.urdashboard.notification

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.util.Log
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsDataSource
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsRepository
import com.gogreenyellow.pglab.urdashboard.main.MainActivity
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.util.TokenUtil

/**
 * Created by Paulina on 2018-02-19.
 */
class RefreshService : JobService() {


    companion object {
        const val ASSIGNED_REVIEWS_NOTIFICATION_ID = 1
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(RefreshService::class.java.simpleName, "onStopJobCalled")
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(RefreshService::class.java.simpleName, "onStartJobCalled")
        checkAssignedSubmissions(params)
        return true
    }

    private fun checkAssignedSubmissions(params: JobParameters?) {
        val token = PreferenceStorage.getInstance(this)?.token ?: return

        if (TextUtils.isEmpty(token) || TokenUtil.getTokenExpiresIn(token) <= 0)
            return

        AssignedSubmissionsRepository.getAssignedSubmissions(token,
                object : AssignedSubmissionsDataSource.AssignedSubmissionsCallback {
                    @SuppressLint("NewApi")
                    override fun gotAssignedSubmissions(assignedSubmission: List<AssignedSubmission>, containNew: Boolean) {
                        if (containNew) {
                            val channelId = "submission_assigned"
                            val notificationMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val channel = NotificationChannel(channelId, "Channel name", NotificationManager.IMPORTANCE_HIGH)
                                notificationMgr.createNotificationChannel(channel)
                            }

                            val builder = NotificationCompat.Builder(this@RefreshService, channelId)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle(getString(R.string.app_name))
                                    .setContentText(getString(R.string.n_new_reviews_text))
                                    .setSound(PreferenceStorage.getInstance(this@RefreshService)!!.newAssignmentSound)
                                    .setAutoCancel(true)

                            val intent = Intent(this@RefreshService, MainActivity::class.java)
                            val pendingIntent = PendingIntent.getActivity(this@RefreshService,
                                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                            builder.setContentIntent(pendingIntent)

                            notificationMgr.notify(ASSIGNED_REVIEWS_NOTIFICATION_ID, builder.build())
                        }
                        jobFinished(params, false)
                    }

                    override fun failedToGetAssignedSubmissions(code: Int) {
                        jobFinished(params, false)
                    }
                })
    }
}