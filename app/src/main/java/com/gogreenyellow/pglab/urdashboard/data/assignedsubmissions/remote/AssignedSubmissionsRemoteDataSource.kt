package com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.remote

import com.android.volley.Response
import com.gogreenyellow.pglab.urdashboard.Token
import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.api.AuthorizedJsonArrayRequest
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsDataSource
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission

/**
 * Created by Paulina on 2018-02-13.
 */
object AssignedSubmissionsRemoteDataSource : AssignedSubmissionsDataSource {
    override fun getAssignedSubmissions(callback: AssignedSubmissionsDataSource.AssignedSubmissionsCallback) {
        val request = AuthorizedJsonArrayRequest(Token.token,
                "https://review-api.udacity.com/api/v1/me/submissions/assigned.json",
                Response.Listener {
                    val response = ArrayList<AssignedSubmission>()

                    for (i in 0 until it.length()) {
                        val assignedJson = it.getJSONObject(i)
                        val id = assignedJson.getLong("id")
                        val assignedAt = assignedJson.getString("assigned_at")
                        val language = assignedJson.getString("language")
                        val projectId = assignedJson.getJSONObject("project").getLong("id")
                        val projectName = assignedJson.getJSONObject("project").getString("name")

                        response.add(AssignedSubmission(id, assignedAt, language, projectId, projectName))
                    }
                    callback.gotAssignedSubmissions(response)
                },
                Response.ErrorListener {
                    //TODO: handle the errors
                    callback.failedToGetAssignedSubmissions(0)
                })
        URDashboard.INSTANCE.requestQueue.add(request)
    }
}