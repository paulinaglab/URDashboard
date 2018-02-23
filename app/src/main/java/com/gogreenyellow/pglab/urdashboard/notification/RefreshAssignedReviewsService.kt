package com.gogreenyellow.pglab.urdashboard.notification

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsDataSource
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsRepository
import com.gogreenyellow.pglab.urdashboard.main.MainActivity
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.util.TokenUtil
import java.lang.reflect.Type

/**
 * Created by Paulina on 2018-02-19.
 */
class RefreshAssignedReviewsService : RefreshService() {


    companion object {
        const val JOB_ID = 1
        const val NOTIFICATION_ID = 1
        val LOG_TAG = RefreshAssignedReviewsService::class.java.simpleName
        fun schedule(context: Context) {
            RefreshService.schedule(context, JOB_ID, RefreshAssignedReviewsService::class.java)
        }

        fun cancel(context: Context) {
            RefreshService.cancel(context, JOB_ID)
        }
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(LOG_TAG, "Job started.")
        return checkAssignedSubmissions(params)
    }

    private fun checkAssignedSubmissions(params: JobParameters?): Boolean {
        val token = PreferenceStorage.getInstance(this)?.token ?: return false

        if (TextUtils.isEmpty(token) || TokenUtil.getTokenExpiresIn(token) <= 0)
            return false

        AssignedSubmissionsRepository.getAssignedSubmissions(token,
                object : AssignedSubmissionsDataSource.AssignedSubmissionsCallback {
                    @SuppressLint("NewApi")
                    override fun gotAssignedSubmissions(assignedSubmission: List<AssignedSubmission>, containNew: Boolean) {
                        Log.i(LOG_TAG, "Got assigned submissions")
                        if (containNew) {
                            Log.i(LOG_TAG, "A new submission")
                            val sound = PreferenceStorage.getInstance(this@RefreshAssignedReviewsService)!!.newAssignmentSound
                            displayNotification(
                                    NOTIFICATION_ID,
                                    R.drawable.ic_notif_default,
                                    R.string.n_new_reviews_title,
                                    R.string.n_new_reviews_text,
                                    sound,
                                    ContextCompat.getColor(this@RefreshAssignedReviewsService, R.color.custom_purple))
                        }
                        Log.i(LOG_TAG, "Job finished")
                        jobFinished(params, false)
                    }

                    override fun failedToGetAssignedSubmissions(code: Int) {
                        Log.i(LOG_TAG, "Job failed")
                        jobFinished(params, false)
                    }
                })
        return true
    }
}