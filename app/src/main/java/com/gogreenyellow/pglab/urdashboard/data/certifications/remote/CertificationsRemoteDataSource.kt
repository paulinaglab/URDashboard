package com.gogreenyellow.pglab.urdashboard.data.certifications.remote

import com.android.volley.Response
import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.api.AuthorizedJsonArrayRequest
import com.gogreenyellow.pglab.urdashboard.data.certifications.CertificationsDataSource
import com.gogreenyellow.pglab.urdashboard.model.Certification

/**
 * Created by Paulina on 2018-02-14.
 */
object CertificationsRemoteDataSource : CertificationsDataSource {


    override fun getCertifications(token: String,
                                   forceRefresh: Boolean,
                                   callback: CertificationsDataSource.CertificationsCallback) {
        val request = AuthorizedJsonArrayRequest(token,
                "https://review-api.udacity.com/api/v1/me/certifications.json",
                Response.Listener {
                    val response = ArrayList<Certification>()
                    for (i in 0 until it.length()) {
                        val certJson = it.getJSONObject(i)
                        val projectId = certJson.getLong("project_id")
                        val status = certJson.getString("status")
                        val active = certJson.getBoolean("active")
                        val projectJson = certJson.getJSONObject("project")
                        val projectName = projectJson.getString("name")
                        val projectPrice = projectJson.getDouble("price")

                        response.add(Certification(projectId, status, active, projectName, projectPrice.toFloat()))
                    }
                    callback.gotCertifications(response, false)
                },
                Response.ErrorListener {
                    //TODO: handle the errors
                    callback.failedToGetCertifications(0)
                })
        URDashboard.INSTANCE.requestQueue.add(request)
    }

    override fun saveCertifications(certifications: List<Certification>, callback: CertificationsDataSource.SaveCertificationsCallback) {
        throw RuntimeException("not supported by remote repo")
    }

    override fun getCertifications(): List<Certification> {
        throw RuntimeException("not supported by remote repo")
    }
}