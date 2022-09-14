package com.akelius.section.countryimages.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akelius.service.extensions.InternetCheck
import com.akelius.service.extensions.Resource
import com.akelius.service.model.countryimagesmodel.File
import com.akelius.service.model.countryimagesmodel.FileData
import com.akelius.service.model.countryimagesmodel.ImageDataClass
import com.akelius.service.model.countryimagesmodel.ImagesDao
import com.akelius.service.repository.CountryImagesRerpository
import com.akelius.service.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CountryImageViewModel
@Inject constructor(
    private val countryImagesRerpository: CountryImagesRerpository,
    private val imagesDao: ImagesDao
) : ViewModel() {
    val _countryimageslist = MutableStateFlow<Resource<ImageDataClass>>(Resource.onLoading())
    val _countryimageslocallist = MutableStateFlow<Resource<ImageDataClass>>(Resource.onLoading())
    val listlocal = ArrayList<Int>()
    val listremote = ArrayList<Int>()
    val countryimageslist: StateFlow<Resource<ImageDataClass>> get() = _countryimageslist
    val countryimageslocallylist: StateFlow<Resource<ImageDataClass>> get() = _countryimageslocallist

    @RequiresApi(Build.VERSION_CODES.M)
    fun getImageRemotelyData(context: Context) {
        viewModelScope.launch {
            _countryimageslist?.value = Resource.onLoading()
            countryImagesRerpository.getImageData(context)
                .catch { e ->
                    e.message?.let { Log.d("Noshairam", it)
                    }
                }.collect {
                    listlocal.clear()
                    listremote.clear()
                    if (countryimageslocallylist.value.data?.files != null) {
                        countryimageslocallylist.value.data?.files?.map { it1 ->
                            listlocal.add(it1.stats.ino)
                        }
                    }
                    it.files.map {
                        listremote.add(it.stats.ino)
                    }
                    if (it.files.isNotEmpty())
                        if (imagesDao.getAll()?.isEmpty() == true)
                            setlocally(it.files)
                        else {
                            Utils.remote.addAll(listremote.minus(listlocal).toList())
                            Utils.local.addAll(listlocal.minus(listremote))
                        }
                    Log.d("findremote", Utils.remote.toString())
                    Log.d("findlocal", Utils.local.toString())

                    _countryimageslist?.value = Resource.onSuccess(it)
                }
        }
    }
    suspend fun setlocally(files: List<File>) {
        files.forEach {
            val convert = FileData(
                it.path,
                it.stats.atime,
                it.stats.mtimeMs,
                it.stats.birthtime,
                it.stats.birthtimeMs,
                it.stats.blksize,
                it.stats.blocks,
                it.stats.ctime,
                it.stats.ctimeMs,
                it.stats.dev,
                it.stats.gid,
                it.stats.ino,
                it.stats.mode,
                it.stats.mtime,
                it.stats.mtimeMs,
                it.stats.nlink,
                it.stats.rdev,
                it.stats.size,
                it.stats.uid
            )
            imagesDao.insertAllImages(convert)
        }

    }

    fun getImageLocallyData() {
        viewModelScope.launch {
            _countryimageslocallist?.value = Resource.onLoading()
            countryImagesRerpository.getallImagesLocally()
                .catch { e ->
                    e.message?.let { Log.d("Noshairam", it) }
                }.collect {
                    _countryimageslocallist?.value = Resource.onSuccess(it)
                }
        }

    }

}