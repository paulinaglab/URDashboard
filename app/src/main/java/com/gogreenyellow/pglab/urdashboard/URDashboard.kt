package com.gogreenyellow.pglab.urdashboard

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsRepository
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsRepository

/**
 * Created by Paulina on 2018-02-13.
 */
class URDashboard : Application() {

    companion object {
        var instance: URDashboard? = null
    }

    lateinit var requestQueue: RequestQueue
    val submissionRequestRepository = SubmissionRequestsRepository()
    val assignedSubmissionsRepository = AssignedSubmissionsRepository()

    override fun onCreate() {
        super.onCreate()
        instance = this
        requestQueue = Volley.newRequestQueue(this)
    }

}