package com.gogreenyellow.pglab.urdashboard.data.submissionrequests

import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest

/**
 * Created by Paulina on 2018-02-13.
 */
interface SubmissionRequestsDataSource {

    fun getActiveSubmissionRequests(token: String, callback: SubmissionRequestsCallback)
    fun refreshSubmissionRequests(token: String, callback: RefreshCallback)

    interface SubmissionRequestsCallback {
        fun gotSubmissionsRequests(response: ArrayList<SubmissionRequest>)
        fun failedToGetSubmissionRequest(code: Int)
    }

    interface RefreshCallback {
        fun refreshFinished()
    }
}