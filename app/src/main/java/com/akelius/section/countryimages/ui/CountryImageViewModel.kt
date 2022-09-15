package com.akelius.section.countryimages.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akelius.service.extensions.InternetCheck
import com.akelius.service.extensions.Resource
import com.akelius.service.model.countryimagesmodel.*
import com.akelius.service.repository.CountryImagesRerpository
import com.akelius.service.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@HiltViewModel
class CountryImageViewModel
@Inject constructor(
    private val countryImagesRerpository: CountryImagesRerpository,
    private val imagesDao: ImagesDao
) : ViewModel() {
    val preparedList = MutableStateFlow<Resource<TreeMap<Int, FileCheck>>>(Resource.onLoading())
    var prepa = TreeMap<Int, FileCheck>()
    val _countryimageslocallist = MutableStateFlow<Resource<ImageDataClass>>(Resource.onLoading())
    val listlocal = ArrayList<Int>()
    val listremote = ArrayList<Int>()

    val countryimageslocallylist: StateFlow<Resource<ImageDataClass>> get() = _countryimageslocallist

    @RequiresApi(Build.VERSION_CODES.M)
    fun getImageRemotelyData(context: Context) {
        viewModelScope.launch {
            preparedList?.value = Resource.onLoading()
            preparedList.value = Resource.onLoading()
            prepa.clear()
            countryImagesRerpository.getImageData(context)
                .catch { e ->
                    e.message?.let {
                        Log.d("Noshairam", it)
                    }
                }.collect {
                    prepa.clear()
                    prepa = getupdatedlistwithtags(it)

                    preparedList.value = Resource.onSuccess(prepa)

                }
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

    suspend fun setlocally(files: List<File>) {
        files.forEach {
            val convert = FileData(//we can also write type converters
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


    suspend fun getupdatedlistwithtags(it: ImageDataClass): TreeMap<Int, FileCheck> {
        var count = 0
        prepa.clear()
        listlocal.clear()
        listremote.clear()
        Utils.remote.clear()
        Utils.local.clear()
        if (countryimageslocallylist.value.data?.files != null) {
            countryimageslocallylist.value.data?.files?.map { it1 ->
                listlocal.add(it1.stats.ino)
            }
        }
        it.files.map { a ->

            listremote.add(a.stats.ino)
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
        var j = 0
        count = 0
        it.files.forEach {
            if (Utils.remote.isNotEmpty()) {
                if (Utils.remote.get(count) == it.stats.ino) {

                    prepa.set(
                        j, FileCheck(
                            "Added R Missing L",
                            it
                        )
                    )
                    if (Utils.remote.size - 1 != count)
                        count++
                } else {
                    prepa.set(
                        j, FileCheck(
                            "",
                            it
                        )
                    )
                }
                j++
            } else {
                prepa.set(
                    j, FileCheck(
                        "",
                        it
                    )
                )
                j++
            }
        }
        countryimageslocallylist.collect { local ->
            count = 0

            local.data?.files?.forEach { data ->
                if (Utils.local.isNotEmpty()) {

                    if (Utils.local.get(count) == data.stats.ino) {
                        prepa.set(
                            j, FileCheck(
                                "Mssing R Added L",
                                data
                            )
                        )
                        if (Utils.local.size - 1 != count)
                            count++
                        j++
                    }
                }
                if (j < it.files.size)
                    if (it.files.get(j).stats?.ino == data.stats.ino && !it.files.get(
                            j
                        ).stats.equals(data.stats)
                    ) {
                        prepa.set(
                            j, FileCheck(
                                "Stat Change",
                                data
                            )
                        )
                    }
            }
            preparedList.value = Resource.onSuccess(prepa)
        }

    }


}