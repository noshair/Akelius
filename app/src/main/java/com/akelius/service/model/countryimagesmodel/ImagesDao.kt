package com.akelius.service.model.countryimagesmodel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ImagesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllImages(images: FileData?)

    @Query("SELECT * FROM file")
   suspend fun getAll(): List<FileData?>?
    @Query("DELETE FROM  file")
    suspend fun deleteAll()
}