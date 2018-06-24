package com.gogreenyellow.pglab.urdashboard.data.submissionrequests

import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.remote.SubmissionRequestsRemoteDataSource

/**
 * Created by Paulina on 2018-02-13.
 */
object SubmissionRequestsRepository : SubmissionRequestsDataSource {
    override fun refreshSubmissionRequests(token: String, callback: SubmissionRequestsDataSource.RefreshCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getActiveSubmissionRequests(token: String, callback: SubmissionRequestsDataSource.SubmissionRequestsCallback) {
        SubmissionRequestsRemoteDataSource.getActiveSubmissionRequests(token, callback)
    }
}