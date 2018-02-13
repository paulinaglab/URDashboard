package com.gogreenyellow.pglab.urdashboard.model

/**
 * Created by Paulina on 2018-02-13.
 */
class SubmissionRequest(val id: Long,
                        val closedAt: String,
                        val status: String,
                        val projects: List<Long>) {
}