package com.gogreenyellow.pglab.urdashboard.data.submissionrequests

import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.remote.SubmissionRequestsRemoteDataSource

/**
 * Created by Paulina on 2018-02-13.
 */
class SubmissionRequestsRepository : SubmissionRequestsDataSource {

    private val remoteSubmissionRequestSource = SubmissionRequestsRemoteDataSource()

    override fun getActiveSubmissionRequests(callback: SubmissionRequestsDataSource.SubmissionRequestsCallback) {
        remoteSubmissionRequestSource.getActiveSubmissionRequests(callback)
    }
}