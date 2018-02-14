package com.gogreenyellow.pglab.urdashboard.data.certifications

import com.gogreenyellow.pglab.urdashboard.data.certifications.local.CertificationsLocalDataSource
import com.gogreenyellow.pglab.urdashboard.data.certifications.remote.CertificationsRemoteDataSource
import com.gogreenyellow.pglab.urdashboard.model.Certification

/**
 * Created by Paulina on 2018-02-14.
 */
object CertificationsRepository : CertificationsDataSource {

    override fun getCertifications(forceRefresh: Boolean,
                                   callback: CertificationsDataSource.CertificationsCallback) {
        if (forceRefresh) {
            CertificationsRemoteDataSource.getCertifications(false,
                    object : CertificationsDataSource.CertificationsCallback {
                        override fun gotCertifications(certifications: List<Certification>) {
                            CertificationsLocalDataSource.saveCertifications(certifications,
                                    object : CertificationsDataSource.SaveCertificationsCallback {
                                        override fun certificationsSaved() {
                                            callback.gotCertifications(certifications)
                                        }

                                        override fun failedToSaveCertifications(errorCode: Int) {
                                            callback.gotCertifications(certifications)
                                        }
                                    })
                        }

                        override fun failedToGetCertifications(errorCode: Int) {
                            callback.failedToGetCertifications(errorCode)
                        }

                    })
        } else {
            CertificationsLocalDataSource.getCertifications(false,
                    object : CertificationsDataSource.CertificationsCallback {
                        override fun gotCertifications(certifications: List<Certification>) {
                            if (certifications.isEmpty()) {
                                getCertifications(true, callback)
                            } else {
                                callback.gotCertifications(certifications)
                            }
                        }

                        override fun failedToGetCertifications(errorCode: Int) {
                            callback.failedToGetCertifications(errorCode)
                        }
                    })
        }
    }

    override fun saveCertifications(certifications: List<Certification>, callback: CertificationsDataSource.SaveCertificationsCallback) {
        CertificationsLocalDataSource.saveCertifications(certifications, callback)
    }
}