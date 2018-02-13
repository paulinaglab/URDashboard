package com.gogreenyellow.pglab.urdashboard.data.submissionrequests

import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest

/**
 * Created by Paulina on 2018-02-13.
 */
interface SubmissionRequestsDataSource {

    fun getActiveSubmissionRequests(callback: SubmissionRequestsCallback)

    interface SubmissionRequestsCallback {
        fun gotSubmissionsRequests(response: ArrayList<SubmissionRequest>)
        fun failedToGetSubmissionRequest(code: Int)
    }
}