package com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.local

import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsDataSource
import com.gogreenyellow.pglab.urdashboard.database.AssignedSubmissionsDao

/**
 * Created by Paulina on 2018-02-18.
 */
object AssignedSubmissionsLocalDataSource : AssignedSubmissionsDataSource {

    private var assignedSubmissionsDao: AssignedSubmissionsDao = URDashboard.INSTANCE.database.assignedSubmissionsDao();

    override fun getAssignedSubmissions(callback: AssignedSubmissionsDataSource.AssignedSubmissionsCallback) {
        callback.gotAssignedSubmissions(assignedSubmissionsDao.getAllAssignedSubmissions())
    }
}