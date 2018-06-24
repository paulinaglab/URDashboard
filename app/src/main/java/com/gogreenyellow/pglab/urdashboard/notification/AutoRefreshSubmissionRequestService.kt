package com.gogreenyellow.pglab.urdashboard.notification

import android.app.job.JobParameters
import android.content.Context
import android.text.TextUtils
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsDataSource.RefreshCallback
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsRepository
import com.gogreenyellow.pglab.urdashboard.util.TokenUtil

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

    override fun onStartJob(params: JobParameters?): Boolean {
        return refreshActiveRequests(params)
    }

    private fun refreshActiveRequests(params: JobParameters?): Boolean {
        val token = PreferenceStorage.getInstance(this)?.token ?: return false

        if (TextUtils.isEmpty(token) || TokenUtil.getTokenExpiresIn(token) <= 0)
            return false

        SubmissionRequestsRepository.refreshSubmissionRequests(token, object : RefreshCallback {
            override fun refreshFinished() {
                jobFinished(params, false)
            }

        })

        return true
    }
}