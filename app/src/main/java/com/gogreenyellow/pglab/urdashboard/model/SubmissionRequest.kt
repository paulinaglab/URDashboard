package com.gogreenyellow.pglab.urdashboard.model

/**
 * Created by Paulina on 2018-02-13.
 */
class SubmissionRequest(val id: Long,
                        val createdAt: String,
                        val closedAt: String,
                        val updatedAt: String,
                        val status: String,
                        val projects: List<Long>) {
}