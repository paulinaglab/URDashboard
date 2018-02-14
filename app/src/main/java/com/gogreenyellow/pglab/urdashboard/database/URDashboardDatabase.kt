package com.gogreenyellow.pglab.urdashboard.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.model.Certification

/**
 * Created by Paulina on 2018-02-14.
 */
@Database(entities = arrayOf(Certification::class, AssignedSubmission::class), version = 1)
abstract class URDashboardDatabase : RoomDatabase() {

    abstract fun certificationsDao(): CertificationsDao
}