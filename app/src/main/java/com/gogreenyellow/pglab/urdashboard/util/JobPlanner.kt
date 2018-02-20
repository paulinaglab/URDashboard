package com.gogreenyellow.pglab.urdashboard.util

import android.content.Context
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.notification.RefreshAssignedReviewsService
import com.gogreenyellow.pglab.urdashboard.notification.RefreshPricesService

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
    }
}