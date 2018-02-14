package com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions

import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.remote.AssignedSubmissionsRemoteDataSource

/**
 * Created by Paulina on 2018-02-13.
 */
object AssignedSubmissionsRepository : AssignedSubmissionsDataSource {

    override fun getAssignedSubmissions(callback: AssignedSubmissionsDataSource.AssignedSubmissionsCallback) {
        AssignedSubmissionsRemoteDataSource.getAssignedSubmissions(callback)
    }
}