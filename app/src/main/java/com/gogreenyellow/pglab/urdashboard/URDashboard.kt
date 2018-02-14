package com.gogreenyellow.pglab.urdashboard

import android.app.Application
import android.arch.persistence.room.Room
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.gogreenyellow.pglab.urdashboard.database.URDashboardDatabase

/**
 * Created by Paulina on 2018-02-13.
 */
class URDashboard : Application() {

    companion object {
        lateinit var INSTANCE: URDashboard
            private set
    }

    lateinit var requestQueue: RequestQueue
    lateinit var database: URDashboardDatabase

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        requestQueue = Volley.newRequestQueue(this)
        database = Room.databaseBuilder(this, URDashboardDatabase::class.java, "database")
                .allowMainThreadQueries().build()
    }

}