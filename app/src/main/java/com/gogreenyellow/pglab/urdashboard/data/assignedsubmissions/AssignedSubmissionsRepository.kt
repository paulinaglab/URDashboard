package com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions

import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.remote.AssignedSubmissionsRemoteDataSource

/**
 * Created by Paulina on 2018-02-13.
 */
class AssignedSubmissionsRepository : AssignedSubmissionsDataSource {

    private var remoteAssignedSubmissionsSource = AssignedSubmissionsRemoteDataSource()

    override fun getAssignedSubmissions(callback: AssignedSubmissionsDataSource.AssignedSubmissionsCallback) {
        remoteAssignedSubmissionsSource.getAssignedSubmissions(callback)
    }
}