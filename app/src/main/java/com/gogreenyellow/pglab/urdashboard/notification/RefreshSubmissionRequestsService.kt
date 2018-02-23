package com.gogreenyellow.pglab.urdashboard.notification

import android.app.job.JobParameters
import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsDataSource
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsRepository
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest
import com.gogreenyellow.pglab.urdashboard.util.TokenUtil

/**
 * Created by Paulina on 2018-02-22.
 */
class RefreshSubmissionRequestsService : RefreshService() {

    companion object {
        const val JOB_ID = 3
        const val NOTIFICATION_ID = 100
        val LOG_TAG = RefreshSubmissionRequestsService::class.java.simpleName
        fun schedule(context: Context) {
            RefreshService.schedule(context, JOB_ID, RefreshSubmissionRequestsService::class.java)
        }

        fun cancel(context: Context) {
            RefreshService.cancel(context, JOB_ID)
        }
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(LOG_TAG, "Job started")

        val token = PreferenceStorage.getInstance(this)?.token
        if (TextUtils.isEmpty(token) || TokenUtil.getTokenExpiresIn(token!!) <= 0)
            return false

        SubmissionRequestsRepository.getActiveSubmissionRequests(token, object : SubmissionRequestsDataSource.SubmissionRequestsCallback {
            override fun gotSubmissionsRequests(response: ArrayList<SubmissionRequest>) {
                Log.i(LOG_TAG, "Got submission requests")

                if (submissionRequestsInconsistent(response)) {
                    val sound = PreferenceStorage.getInstance(this@RefreshSubmissionRequestsService)!!.requestIncorrectSound

                    displayNotification(NOTIFICATION_ID,
                            R.drawable.ic_notif_error,
                            R.string.n_incorrect_request_title,
                            R.string.n_incorrect_request_text,
                            sound,
                            ContextCompat.getColor(this@RefreshSubmissionRequestsService, R.color.custom_blue))
                    Log.i(LOG_TAG, "Requests inconsistent")
                }
                Log.i(LOG_TAG, "Job finished")
                jobFinished(params, false)
            }

            override fun failedToGetSubmissionRequest(code: Int) {
                Log.i(LOG_TAG, "Job failed")
                jobFinished(params, true)
            }

        })
        return true
    }


    fun submissionRequestsInconsistent(requests: List<SubmissionRequest>): Boolean {
        if (requests.isEmpty())
            return false

        val firstRequest = requests[0]
        for (request in requests) {
            val firstProjectList = firstRequest.projects
            val requestProjects = request.projects
            if (!(firstProjectList.containsAll(requestProjects) && firstProjectList.size == requestProjects.size)) {
                return true
            }
        }

        return false
    }

}