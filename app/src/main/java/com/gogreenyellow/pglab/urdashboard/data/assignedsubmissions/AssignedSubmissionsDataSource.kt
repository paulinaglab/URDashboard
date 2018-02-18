package com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions

import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission

/**
 * Created by Paulina on 2018-02-13.
 */
interface AssignedSubmissionsDataSource {

    companion object {
        val ERROR_CODE_NON_EXISTENT = 100;
    }


    fun saveAssignedSubmission(assigned: AssignedSubmission)
    fun getAssignedSubmissions(callback: AssignedSubmissionsCallback)
    fun getAssignedSubmissionBySubmissionIdAndDate(id: Long, time: String): AssignedSubmission?


    interface AssignedSubmissionsCallback {
        fun gotAssignedSubmissions(assignedSubmission: List<AssignedSubmission>, containNew: Boolean)
        fun failedToGetAssignedSubmissions(code: Int)
    }

    interface AssignedSubmissionCallback {
        fun gotAssignedSubmission(assignedSubmission: AssignedSubmission)
        fun failedToGetAssignedSubmissions(code: Int)
    }
}