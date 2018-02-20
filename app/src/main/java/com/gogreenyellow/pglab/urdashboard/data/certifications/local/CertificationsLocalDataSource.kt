package com.gogreenyellow.pglab.urdashboard.data.certifications.local

import com.gogreenyellow.pglab.urdashboard.URDashboard
import com.gogreenyellow.pglab.urdashboard.data.certifications.CertificationsDataSource
import com.gogreenyellow.pglab.urdashboard.model.Certification

/**
 * Created by Paulina on 2018-02-14.
 */
object CertificationsLocalDataSource : CertificationsDataSource {

    private var certificationsDao = URDashboard.INSTANCE.database.certificationsDao()

    override fun getCertifications(token: String,
                                   forceRefresh: Boolean,
                                   callback: CertificationsDataSource.CertificationsCallback) {
        //TODO: asynchronously
        callback.gotCertifications(certificationsDao.getAllCertifications(), false)
    }

    override fun saveCertifications(certifications: List<Certification>,
                                    callback: CertificationsDataSource.SaveCertificationsCallback) {
        //TODO: asynchronously
        certificationsDao.deleteAllCertifications()
        for (certification in certifications) {
            certificationsDao.insertCertification(certification)
        }
    }

    override fun getCertifications(): List<Certification> {
        return certificationsDao.getAllCertifications()
    }
}