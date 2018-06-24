package com.gogreenyellow.pglab.urdashboard.data.submissionrequests.remote

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.api.AuthorizedJsonArrayRequest
import com.gogreenyellow.pglab.urdashboard.api.AuthorizedJsonObjectRequest
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsDataSource
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsDataSource.RefreshCallback
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsDataSource.SubmissionRequestsCallback
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.CountDownLatch
import javax.security.auth.callback.Callback

/**
 * Created by Paulina on 2018-02-13.
 */
object SubmissionRequestsRemoteDataSource : SubmissionRequestsDataSource {


    override fun getActiveSubmissionRequests(token: String, callback: SubmissionRequestsCallback) {
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

    override fun refreshSubmissionRequests(token: String, callback: RefreshCallback) {
        getActiveSubmissionRequests(token, object : SubmissionRequestsCallback {
            override fun gotSubmissionsRequests(response: ArrayList<SubmissionRequest>) {
                val latch = CountDownLatch(response.size)
                for (submissionRequest in response) {
                    val body = generateUpdateRequestBody(submissionRequest)
                    val updateRequest = AuthorizedJsonObjectRequest(token, Request.Method.PUT,
                            "https://review-api.udacity.com/api/v1/submission_requests/" + submissionRequest.id + ".json",
                            body,
                            Response.Listener {
                                requestFinished(callback, latch)
                            },
                            Response.ErrorListener {
                                requestFinished(callback, latch)
                                //TODO: ignoring for now, notification in the future
                            })
                    URDashboard.INSTANCE.requestQueue.add(updateRequest)
                }
            }

            override fun failedToGetSubmissionRequest(code: Int) {
                TODO("probably going to ignore this for now, but maybe some kind of a notification for future?")
            }
        })
    }

    private fun generateUpdateRequestBody(submissionRequest: SubmissionRequest): JSONObject {
        val body = JSONObject()
        val projectsArray = JSONArray()
        for (project in submissionRequest.projects) {
            val projectObject = JSONObject()
            projectObject.put("project_id", project)
            projectObject.put("language", "en-us")      //TODO : the app won't work for non english submission requests for now
            projectsArray.put(projectObject)
        }
        body.put("projects", projectsArray)
        return body
    }

    private fun requestFinished(callback: RefreshCallback, latch: CountDownLatch) {
        latch.countDown()
        if (latch.count == 0L) {
            callback.refreshFinished()
        }
    }
}