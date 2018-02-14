package com.gogreenyellow.pglab.urdashboard.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Paulina on 2018-02-14.
 */
@Entity
class Certification(val projectId: Long,
                    val status: String,
                    val active: Boolean,
                    val projectName: String,
                    val projectPrice: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0;
}