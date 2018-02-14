package com.gogreenyellow.pglab.urdashboard.database

import android.arch.persistence.room.*
import com.gogreenyellow.pglab.urdashboard.model.Certification

/**
 * Created by Paulina on 2018-02-14.
 */
@Dao
interface CertificationsDao {

    @Query("select * from certification")
    fun getAllCertifications(): List<Certification>

    @Query("select * from certification where projectId=:projectId")
    fun getCertificationByProjectId(projectId: Long): Certification?

    @Insert
    fun insertCertification(certification: Certification)

    @Update
    fun updateCertification(certification: Certification)

    @Delete
    fun deleteCertification(certification: Certification)

    @Query("delete from certification")
    fun deleteAllCertifications()
}