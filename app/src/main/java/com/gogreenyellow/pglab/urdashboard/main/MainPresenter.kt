package com.gogreenyellow.pglab.urdashboard.main

import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsDataSource
import com.gogreenyellow.pglab.urdashboard.data.assignedsubmissions.AssignedSubmissionsRepository
import com.gogreenyellow.pglab.urdashboard.data.certifications.CertificationsDataSource
import com.gogreenyellow.pglab.urdashboard.data.certifications.CertificationsRepository
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsDataSource
import com.gogreenyellow.pglab.urdashboard.data.submissionrequests.SubmissionRequestsRepository
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.model.Certification
import com.gogreenyellow.pglab.urdashboard.model.QueuedProject
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest

/**
 * Created by Paulina on 2018-02-13.
 */
class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    override fun start() {
        SubmissionRequestsRepository.getActiveSubmissionRequests(object : SubmissionRequestsDataSource.SubmissionRequestsCallback {
            override fun gotSubmissionsRequests(response: ArrayList<SubmissionRequest>) {
                getCertifications(response)
                view.displaySubmissionRequests(response)
            }

            override fun failedToGetSubmissionRequest(code: Int) {

            }
        })

        AssignedSubmissionsRepository.getAssignedSubmissions(object : AssignedSubmissionsDataSource.AssignedSubmissionsCallback {
            override fun gotAssignedSubmissions(assignedSubmission: List<AssignedSubmission>) {

            }

            override fun failedToGetAssignedSubmissions(code: Int) {

            }
        })
    }

    override fun refreshAll() {
        start()
    }

    private fun getCertifications(submissionRequests: List<SubmissionRequest>) {
        CertificationsRepository.getCertifications(false,
                object : CertificationsDataSource.CertificationsCallback {
                    override fun gotCertifications(certifications: List<Certification>) {
                        val list = ArrayList<QueuedProject>()
                        certifications
                                .filter { it.active && it.status.equals("certified", true) }
                                .mapTo(list) {
                                    QueuedProject(
                                            it.projectId.toString(),
                                            it.projectName,
                                            it.projectPrice,
                                            isProjectRequested(submissionRequests, it.projectId))
                                }
                        view.displayProjectsQueuedFor(list)
                    }

                    override fun failedToGetCertifications(errorCode: Int) {

                    }

                })
    }

    private fun isProjectRequested(submissionRequests: List<SubmissionRequest>,
                                   projectId: Long): Boolean {
        for (sr in submissionRequests) {
            for (p in sr.projects) {
                if (p == projectId)
                    return true
            }
        }
        return false
    }
}