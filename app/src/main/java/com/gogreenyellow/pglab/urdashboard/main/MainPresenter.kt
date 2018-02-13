package com.gogreenyellow.pglab.urdashboard.main

import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsDataSource
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsDataSource
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsRepository
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest

/**
 * Created by Paulina on 2018-02-13.
 */
class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    private val submissionRequestRepository = URDashboard.instance?.submissionRequestRepository
    private val assignedSubmissionsRepository = URDashboard.instance?.assignedSubmissionsRepository

    override fun start() {
        submissionRequestRepository?.getActiveSubmissionRequests(object : SubmissionRequestsDataSource.SubmissionRequestsCallback {
            override fun gotSubmissionsRequests(response: ArrayList<SubmissionRequest>) {
                view.displaySubmissionRequests(response)
            }

            override fun failedToGetSubmissionRequest(code: Int) {

            }
        })

        assignedSubmissionsRepository?.getAssignedSubmissions(object : AssignedSubmissionsDataSource.AssignedSubmissionsCallback {
            override fun gotAssignedSubmissions(assignedSubmission: List<AssignedSubmission>) {
                view.displayAssignedSubmissions(assignedSubmission)
            }

            override fun failedToGetAssignedSubmissions(code: Int) {

            }
        })
    }
}