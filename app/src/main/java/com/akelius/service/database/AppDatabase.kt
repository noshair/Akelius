package com.akelius.service.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akelius.service.model.countryimagesmodel.FileData
import com.akelius.service.model.countryimagesmodel.ImagesDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(
    entities = [FileData::class],
    version = 5,
    exportSchema = false
)
 abstract class AppDatabase : RoomDatabase() {

    abstract fun image():ImagesDao

    companion object {

        private val LOG_TAG = AppDatabase::class.java.simpleName
        val LOCK = Any()
        val DATABASE_NAME = "akelius"

    }

}