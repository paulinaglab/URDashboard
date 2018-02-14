package com.gogreenyellow.pglab.urdashboard.data.certifications

import com.gogreenyellow.pglab.urdashboard.model.Certification

/**
 * Created by Paulina on 2018-02-14.
 */
interface CertificationsDataSource {
    fun getCertifications(forceRefresh: Boolean, callback: CertificationsCallback)

    fun saveCertifications(certifications: List<Certification>, callback: SaveCertificationsCallback)

    interface CertificationsCallback {
        fun gotCertifications(certifications: List<Certification>)
        fun failedToGetCertifications(errorCode: Int)
    }

    interface SaveCertificationsCallback {
        fun certificationsSaved()
        fun failedToSaveCertifications(errorCode: Int)
    }
}