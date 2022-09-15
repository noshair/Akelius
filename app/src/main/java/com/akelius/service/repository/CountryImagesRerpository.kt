package com.akelius.service.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.akelius.service.api_service.ApiService
import com.akelius.service.database.AppDatabase
import com.akelius.service.extensions.InternetCheck
import com.akelius.service.model.countryimagesmodel.*
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

class CountryImagesRerpository @Inject constructor(
    private val apiService: ApiService,
    private val imagesDao: ImagesDao
) {
    @RequiresApi(Build.VERSION_CODES.M)
    fun getImageData(context: Context): Flow<ImageDataClass> = flow {
        if (InternetCheck.isOnline(context)) {
            emit(apiService.countriesImage())
        } else {
            getallImagesLocally().collect(){
                emit( it)
            }
        }
    }.flowOn(Dispatchers.IO)

    fun getallImagesLocally(): Flow<ImageDataClass> = flow {
        val myList: MutableList<File> = mutableListOf<File>()

        val filelist: ArrayList<File>? = null
        imagesDao.getAll()?.forEach {
            if (it != null) {
                val stats = Stats(
                    it.atime,
                    it.mtimeMs,
                    it.birthtime,
                    it.birthtimeMs,
                    it.blksize,
                    it.blocks,
                    it.ctime,
                    it.ctimeMs,
                    it.dev,
                    it.gid,
                    it.ino,
                    it.mode,
                    it.mtime,
                    it.mtimeMs,
                    it.nlink,
                    it.rdev,
                    it.size,
                    it.uid
                )
                val file = File(
                    it.path,
                    stats
                )
                filelist?.add(file)
                myList.add(file)
            }
        }
        if (myList != null) {
            Log.d("diffrence", myList.size.toString())
            emit(ImageDataClass(myList, "200")
            )
        }
    }
}