package com.gogreenyellow.pglab.urdashboard.data.certifications

import com.gogreenyellow.pglab.urdashboard.data.certifications.local.CertificationsLocalDataSource
import com.gogreenyellow.pglab.urdashboard.data.certifications.remote.CertificationsRemoteDataSource
import com.gogreenyellow.pglab.urdashboard.model.Certification

/**
 * Created by Paulina on 2018-02-14.
 */
object CertificationsRepository : CertificationsDataSource {

    override fun getCertifications(token: String,
                                   forceRefresh: Boolean,
                                   callback: CertificationsDataSource.CertificationsCallback) {
        if (forceRefresh) {
            CertificationsRemoteDataSource.getCertifications(token, true,
                    object : CertificationsDataSource.CertificationsCallback {
                        override fun gotCertifications(certifications: List<Certification>, changes: Boolean) {
                            val localCerts = CertificationsLocalDataSource.getCertifications()
                            var newPrice = false;

                            for (localCert in localCerts) {
                                certifications
                                        .filter {
                                            localCert.projectId == it.projectId
                                                    && localCert.projectPrice != it.projectPrice
                                                    && it.status.equals("certified")
                                        }
                                        .forEach { newPrice = true }
                            }

                            CertificationsLocalDataSource.saveCertifications(certifications,
                                    object : CertificationsDataSource.SaveCertificationsCallback {
                                        override fun certificationsSaved() {
                                            callback.gotCertifications(certifications, newPrice)
                                        }

                                        override fun failedToSaveCertifications(errorCode: Int) {
                                            callback.gotCertifications(certifications, newPrice)
                                        }
                                    })
                        }

                        override fun failedToGetCertifications(errorCode: Int) {
                            callback.failedToGetCertifications(errorCode)
                        }

                    })
        } else {
            CertificationsLocalDataSource.getCertifications(token, false,
                    object : CertificationsDataSource.CertificationsCallback {
                        override fun gotCertifications(certifications: List<Certification>, changes: Boolean) {
                            if (certifications.isEmpty()) {
                                getCertifications(token, true, callback)
                            } else {
                                callback.gotCertifications(certifications, changes)
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

    override fun getCertifications(): List<Certification> {
        throw RuntimeException("not supported")
    }
}