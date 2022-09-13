package com.akelius.service.database

import android.content.Context
import androidx.room.Room
import com.akelius.service.model.countryimagesmodel.ImagesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): ImagesDao {
        return appDatabase.image()
    }
    @Singleton
    private var sInstance: AppDatabase? = null

    @Singleton
    @Provides
    fun getInstance(@ApplicationContext context: Context): AppDatabase {
        if (sInstance == null) {
            synchronized(AppDatabase.LOCK) {
                sInstance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    AppDatabase.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
        return sInstance!!
    }
}