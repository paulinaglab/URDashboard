package com.gogreenyellow.pglab.urdashboard.notification

import android.app.job.JobParameters
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.data.certifications.CertificationsDataSource
import com.gogreenyellow.pglab.urdashboard.data.certifications.CertificationsRepository
import com.gogreenyellow.pglab.urdashboard.model.Certification
import com.gogreenyellow.pglab.urdashboard.util.TokenUtil

/**
 * Created by Paulina on 2018-02-20.
 */
class RefreshPricesService : RefreshService() {

    companion object {
        const val JOB_ID = 2
        const val NOTIFICATION_ID = 10
        val LOG_TAG = RefreshPricesService::class.java.simpleName
        fun schedule(context: Context) {
            RefreshService.schedule(context, JOB_ID, RefreshPricesService::class.java)
        }

        fun cancel(context: Context) {
            RefreshService.cancel(context, JOB_ID)
        }
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(LOG_TAG, "Job started")
        return getCerts(params)
    }

    private fun getCerts(params: JobParameters?): Boolean {
        val token = PreferenceStorage.getInstance(this)?.token ?: return false
        if (TextUtils.isEmpty(token) || TokenUtil.getTokenExpiresIn(token) <= 0)
            return false

        CertificationsRepository.getCertifications(token, true,
                object : CertificationsDataSource.CertificationsCallback {
                    override fun gotCertifications(certifications: List<Certification>, changes: Boolean) {
                        Log.i(LOG_TAG, "Got certifications")
                        if (changes) {
                            val sound = PreferenceStorage.getInstance(this@RefreshPricesService)!!.priceChangesSound

                            displayNotification(
                                    NOTIFICATION_ID,
                                    R.drawable.ic_notif_price,
                                    R.string.n_price_changes_title,
                                    R.string.n_price_changes_text,
                                    sound)
                            Log.i(LOG_TAG, "New price")
                        }
                        Log.i(LOG_TAG, "Job finished")
                        jobFinished(params, false)
                    }

                    override fun failedToGetCertifications(errorCode: Int) {
                        Log.i(LOG_TAG, "Job failed")
                        jobFinished(params, true)
                    }

                })

        return true
    }
}