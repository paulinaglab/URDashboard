package com.gogreenyellow.pglab.urdashboard.notification

import android.app.job.JobParameters
import android.content.Context

class AutoRefreshSubmissionRequestService : RefreshService() {

    companion object {
        const val JOB_ID = 4
        fun schedule(context: Context) {
            RefreshService.schedule(context, AutoRefreshSubmissionRequestService.JOB_ID, AutoRefreshSubmissionRequestService::class.java)
        }

        fun cancel(context: Context) {
            RefreshService.cancel(context, RefreshSubmissionRequestsService.JOB_ID)
        }
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}