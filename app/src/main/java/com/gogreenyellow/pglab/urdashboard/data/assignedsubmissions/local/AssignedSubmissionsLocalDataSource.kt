package com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.local

import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsDataSource
import com.gogreenyellow.pglab.urdashboard.database.AssignedSubmissionsDao
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission

/**
 * Created by Paulina on 2018-02-18.
 */
object AssignedSubmissionsLocalDataSource : AssignedSubmissionsDataSource {

    private var assignedSubmissionsDao: AssignedSubmissionsDao = URDashboard.INSTANCE.database.assignedSubmissionsDao();

    override fun getAssignedSubmissions(callback: AssignedSubmissionsDataSource.AssignedSubmissionsCallback) {
        callback.gotAssignedSubmissions(assignedSubmissionsDao.getAllAssignedSubmissions(), false)
    }

    override fun getAssignedSubmissionBySubmissionIdAndDate(id: Long, time: String, callback: AssignedSubmissionsDataSource.AssignedSubmissionCallback) {
        val submission = assignedSubmissionsDao.getAssignedSubmissionByIdAndTime(id, time)
        if (submission == null) {
            callback.failedToGetAssignedSubmissions(AssignedSubmissionsDataSource.ERROR_CODE_NON_EXISTENT)
        } else {
            callback.gotAssignedSubmission(submission)
        }
    }

    override fun saveAssignedSubmission(assigned: AssignedSubmission) {
        assignedSubmissionsDao.insertAssignedSubmission(assigned)
    }
}