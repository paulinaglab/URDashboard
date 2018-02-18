package com.gogreenyellow.pglab.urdashboard.data.submissionrequests.remote

import com.android.volley.Response
import com.gogreenyellow.pglab.urdashboard.Token
import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.api.AuthorizedJsonArrayRequest
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsDataSource
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest

/**
 * Created by Paulina on 2018-02-13.
 */
object SubmissionRequestsRemoteDataSource : SubmissionRequestsDataSource {

    override fun getActiveSubmissionRequests(token: String, callback: SubmissionRequestsDataSource.SubmissionRequestsCallback) {
        val request = AuthorizedJsonArrayRequest(token,
                "https://review-api.udacity.com/api/v1/me/submission_requests.json",
                Response.Listener {
                    val response = ArrayList<SubmissionRequest>()

                    for (i in 0 until it.length()) {
                        val submissionRequestJson = it.getJSONObject(i)
                        val id = submissionRequestJson.getLong("id")
                        val createdAt = submissionRequestJson.getString("created_at")
                        val closedAt = submissionRequestJson.getString("closed_at")
                        val updatedAt = submissionRequestJson.getString("updated_at")
                        val status = submissionRequestJson.getString("status")
                        val projectsJsonArray = submissionRequestJson.getJSONArray("submission_request_projects")
                        val projectsList = ArrayList<Long>()

                        for (j in 0 until projectsJsonArray.length()) {
                            val projectJson = projectsJsonArray.getJSONObject(j)
                            val projectId = projectJson.getLong("project_id")
                            projectsList.add(projectId)
                        }
                        response.add(SubmissionRequest(id, createdAt, closedAt, updatedAt, status, projectsList))
                    }

                    callback.gotSubmissionsRequests(response)
                },
                Response.ErrorListener {
                    //TODO handle errors
                    callback.failedToGetSubmissionRequest(0)
                })
        URDashboard.INSTANCE.requestQueue.add(request)
    }
}