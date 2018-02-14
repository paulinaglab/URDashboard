package com.gogreenyellow.pglab.urdashboard.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Paulina on 2018-02-13.
 */
@Entity
class AssignedSubmission(val submissionId: Long,
                         val assignedAt: String,
                         val language: String,
                         val projectId: Long,
                         val projectName: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var seen: Boolean = false
}