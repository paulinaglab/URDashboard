package com.gogreenyellow.pglab.urdashboard.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission

/**
 * Created by Paulina on 2018-02-18.
 */
@Dao
interface AssignedSubmissionsDao {

    @Query("select * from assignedSubmission")
    fun getAllAssignedSubmissions(): List<AssignedSubmission>

    @Query("select * from assignedSubmission where submissionId=:submissionId and assignedAt=:assignedAt")
    fun getAssignedSubmissionByIdAndTime(submissionId: Long, assignedAt: String): AssignedSubmission?

    @Query("delete from assignedSubmission")
    fun deleteAssignedSubmissions()

    @Insert
    fun insertAssignedSubmission(assignedSubmission: AssignedSubmission)
}