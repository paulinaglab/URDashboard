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
                    AssignedSubmissionsLocalDataSource
                            .getAssignedSubmissionBySubmissionIdAndDate(submission.submissionId,
                                    submission.assignedAt,
                                    object : AssignedSubmissionsDataSource.AssignedSubmissionCallback {
                                        override fun gotAssignedSubmission(assignedSubmission: AssignedSubmission) {

                                        }

                                        override fun failedToGetAssignedSubmissions(code: Int) {
                                            AssignedSubmissionsLocalDataSource.saveAssignedSubmission(submission)
                                        }
                                    })
                }

            }

            override fun failedToGetAssignedSubmissions(code: Int) {
                callback.failedToGetAssignedSubmissions(code)
            }

        })
    }

    override fun getAssignedSubmissionBySubmissionIdAndDate(id: Long,
                                                            time: String,
                                                            callback: AssignedSubmissionsDataSource.AssignedSubmissionCallback) {
    }

    override fun saveAssignedSubmission(assigned: AssignedSubmission) {
    }
}