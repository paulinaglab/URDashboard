package com.gogreenyellow.pglab.urdashboard.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.main.MainActivity

/**
 * Created by Paulina on 2018-02-20.
 */
abstract class RefreshService : JobService() {

    companion object {
        fun schedule(context: Context, jobId: Int, serviceClass: Class<*>) {
            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            val componentName = ComponentName(context, serviceClass)
            val jobInfo = JobInfo.Builder(jobId, componentName)
            jobInfo.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPeriodic(PreferenceStorage.getInstance(context)!!.requestInterval)
                    .setPersisted(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                jobInfo.setRequiresBatteryNotLow(false)
                        .setRequiresStorageNotLow(false)
            }

            scheduler.schedule(jobInfo.build())
        }

        fun cancel(context: Context, jobId: Int) {
            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancel(jobId)
        }
    }

    /*

            R.id.mm_notification2 -> displayNotification(
                    ,
                    ,
                    )
            R.id.mm_notification3 -> displayNotification(
                    R.drawable.ic_notif_error,
                    R.string.n_incorrect_request_title,
                    R.string.n_incorrect_request_text)
     */

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    @SuppressLint("NewApi")
    fun displayNotification(notificationId: Int,
                            smallIconResId: Int,
                            contentTitleResId: Int,
                            contentTextResId: Int,
                            sound: Uri) {
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
                .setSound(sound)
                .setAutoCancel(true)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        notificationMgr.notify(notificationId, builder.build())
    }
}