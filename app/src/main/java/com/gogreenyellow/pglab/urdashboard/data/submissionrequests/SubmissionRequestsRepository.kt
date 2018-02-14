package com.gogreenyellow.pglab.urdashboard.data.submissionrequests

import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.remote.SubmissionRequestsRemoteDataSource

/**
 * Created by Paulina on 2018-02-13.
 */
object SubmissionRequestsRepository : SubmissionRequestsDataSource {

    override fun getActiveSubmissionRequests(callback: SubmissionRequestsDataSource.SubmissionRequestsCallback) {
        SubmissionRequestsRemoteDataSource.getActiveSubmissionRequests(callback)
    }
}