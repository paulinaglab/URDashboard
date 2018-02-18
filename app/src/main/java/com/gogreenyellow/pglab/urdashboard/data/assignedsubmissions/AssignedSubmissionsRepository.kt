package com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions

import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.local.AssignedSubmissionsLocalDataSource
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.remote.AssignedSubmissionsRemoteDataSource
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission

/**
 * Created by Paulina on 2018-02-13.
 */
object AssignedSubmissionsRepository : AssignedSubmissionsDataSource {

    override fun getAssignedSubmissions(callback: AssignedSubmissionsDataSource.AssignedSubmissionsCallback) {
        AssignedSubmissionsRemoteDataSource.getAssignedSubmissions(object : AssignedSubmissionsDataSource.AssignedSubmissionsCallback {
            override fun gotAssignedSubmissions(assignedSubmission: List<AssignedSubmission>, containNew: Boolean) {
                for (submission in assignedSubmission) {
                    val localSubmission = AssignedSubmissionsLocalDataSource.getAssignedSubmissionBySubmissionIdAndDate(submission.submissionId,
                            submission.assignedAt)
                    if (localSubmission != null) {
                        callback.gotAssignedSubmissions(assignedSubmission, true)
                        return
                    }
                }
                callback.gotAssignedSubmissions(assignedSubmission, false)
            }

            override fun failedToGetAssignedSubmissions(code: Int) {
                callback.failedToGetAssignedSubmissions(code)
            }

        })
    }

    override fun getAssignedSubmissionBySubmissionIdAndDate(id: Long,
                                                            time: String): AssignedSubmission? {
        return null
    }

    override fun saveAssignedSubmission(assigned: AssignedSubmission) {
    }
}