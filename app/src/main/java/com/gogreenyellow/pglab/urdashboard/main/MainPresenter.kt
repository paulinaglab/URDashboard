package com.gogreenyellow.pglab.urdashboard.main

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
import com.gogreenyellow.pglab.urdashboard.util.TokenUtil

/**
 * Created by Paulina on 2018-02-13.
 */
class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    override fun start() {

    }

    override fun refreshAll(token: String?, force: Boolean) {
        if (token == null || TokenUtil.getTokenExpiresIn(token) <= 0) {
            view.showTokenDialog(false)
            return
        }

        var runningRefresh = 2
        SubmissionRequestsRepository.getActiveSubmissionRequests(token, object : SubmissionRequestsDataSource.SubmissionRequestsCallback {
            override fun gotSubmissionsRequests(response: ArrayList<SubmissionRequest>) {
                getCertifications(token, response, false)
                view.displaySubmissionRequests(response)
                runningRefresh--
                if (runningRefresh == 0)
                    view.hideRefreshing()
            }

            override fun failedToGetSubmissionRequest(code: Int) {
                runningRefresh--
                if (runningRefresh == 0)
                    view.hideRefreshing()
            }
        })

        AssignedSubmissionsRepository.getAssignedSubmissions(token, object : AssignedSubmissionsDataSource.AssignedSubmissionsCallback {
            override fun gotAssignedSubmissions(assignedSubmission: List<AssignedSubmission>, containNew: Boolean) {
                view.displayAssignedSubmissions(assignedSubmission)
                runningRefresh--
                if (runningRefresh == 0)
                    view.hideRefreshing()
            }

            override fun failedToGetAssignedSubmissions(code: Int) {
                runningRefresh--
                if (runningRefresh == 0)
                    view.hideRefreshing()
            }
        })

        view.displayTokenData(TokenUtil.getTokenExpiresIn(token),
                TokenUtil.getTokenExpirationDate(token))
    }

    private fun getCertifications(token: String, submissionRequests: List<SubmissionRequest>, force: Boolean) {
        CertificationsRepository.getCertifications(token, force,
                object : CertificationsDataSource.CertificationsCallback {
                    override fun gotCertifications(certifications: List<Certification>, changes: Boolean) {
                        val list = ArrayList<QueuedProject>()
                        certifications
                                .filter { it.active && it.status.equals("certified", true) }
                                .mapTo(list) {
                                    QueuedProject(
                                            it.projectId,
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
        return submissionRequests
                .flatMap { it.projects }
                .contains(projectId)
    }
}