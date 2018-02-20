package com.gogreenyellow.pglab.urdashboard.data.certifications

import com.gogreenyellow.pglab.urdashboard.model.Certification

/**
 * Created by Paulina on 2018-02-14.
 */
interface CertificationsDataSource {
    fun getCertifications(token: String, forceRefresh: Boolean, callback: CertificationsCallback)

    fun saveCertifications(certifications: List<Certification>, callback: SaveCertificationsCallback)

    fun getCertifications(): List<Certification>

    interface CertificationsCallback {
        fun gotCertifications(certifications: List<Certification>, changes: Boolean)
        fun failedToGetCertifications(errorCode: Int)
    }

    interface SaveCertificationsCallback {
        fun certificationsSaved()
        fun failedToSaveCertifications(errorCode: Int)
    }
}