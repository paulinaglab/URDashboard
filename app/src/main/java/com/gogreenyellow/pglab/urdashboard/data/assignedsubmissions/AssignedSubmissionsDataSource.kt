package com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions

import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission

/**
 * Created by Paulina on 2018-02-13.
 */
interface AssignedSubmissionsDataSource {
    fun getAssignedSubmissions(callback: AssignedSubmissionsCallback)


    interface AssignedSubmissionsCallback {
        fun gotAssignedSubmissions(assignedSubmission: List<AssignedSubmission>)
        fun failedToGetAssignedSubmissions(code: Int)
    }
}