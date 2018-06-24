package com.gogreenyellow.pglab.urdashboard.util

import android.content.Context
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.notification.AutoRefreshSubmissionRequestService
import com.gogreenyellow.pglab.urdashboard.notification.RefreshAssignedReviewsService
import com.gogreenyellow.pglab.urdashboard.notification.RefreshPricesService
import com.gogreenyellow.pglab.urdashboard.notification.RefreshSubmissionRequestsService

/**
 * Created by Paulina on 2018-02-20.
 */
object JobPlanner {

    fun scheduleJobs(context: Context) {
        val storage = PreferenceStorage.getInstance(context);

        if (storage!!.isNotifyNewAssignment)
            RefreshAssignedReviewsService.schedule(context)
        else
            RefreshAssignedReviewsService.cancel(context)

        if (storage.isNotifyPriceChanges)
            RefreshPricesService.schedule(context)
        else
            RefreshPricesService.cancel(context)

        if (storage.isNotifyIncorrectRequest)
            RefreshSubmissionRequestsService.schedule(context)
        else
            RefreshSubmissionRequestsService.cancel(context)

        if (storage.isAutoRefresh)
            AutoRefreshSubmissionRequestService.schedule(context)
        else
            AutoRefreshSubmissionRequestService.cancel(context)
    }
}