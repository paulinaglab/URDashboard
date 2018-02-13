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
class SubmissionRequestsRemoteDataSource : SubmissionRequestsDataSource {

    override fun getActiveSubmissionRequests(callback: SubmissionRequestsDataSource.SubmissionRequestsCallback) {
        val request = AuthorizedJsonArrayRequest(Token.token,
                "https://review-api.udacity.com/api/v1/me/submission_requests.json",
                Response.Listener {
                    val response = ArrayList<SubmissionRequest>()

                    for (i in 0 until it.length()) {
                        val submissionRequestJson = it.getJSONObject(i)
                        val id = submissionRequestJson.getLong("id")
                        val closedAt = submissionRequestJson.getString("closed_at")
                        val status = submissionRequestJson.getString("status")
                        val projectsJsonArray = submissionRequestJson.getJSONArray("submission_request_projects")
                        val projectsList = ArrayList<Long>()

                        for (j in 0..projectsJsonArray.length()) {
                            val projectJson = projectsJsonArray.getJSONObject(i)
                            val projectId = projectJson.getLong("project_id")
                            projectsList.add(projectId)
                        }
                        response.add(SubmissionRequest(id, closedAt, status, projectsList))
                    }

                    callback.gotSubmissionsRequests(response)
                },
                Response.ErrorListener {
                    //TODO handle errors
                    callback.failedToGetSubmissionRequest(0)
                })
        URDashboard.instance?.requestQueue?.add(request)
    }
}