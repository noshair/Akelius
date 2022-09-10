package com.akelius.section.countryimages.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akelius.service.extensions.Resource
import com.akelius.service.model.countryimagesmodel.ImageDataClass
import com.akelius.service.repository.CountryImagesRerpository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryImageViewModel
@Inject constructor(private val countryImagesRerpository: CountryImagesRerpository)
    : ViewModel() {
    val _countryimageslist = MutableStateFlow<Resource<ImageDataClass>>(Resource.onLoading())
    val countryimageslist: StateFlow<Resource<ImageDataClass>> get() = _countryimageslist
    fun getTodayNamazData(
    ) {
        viewModelScope.launch {
            _countryimageslist?.value = Resource.onLoading()
            countryImagesRerpository.getTodayNamazData()
                .catch {
                        e ->
                    e.message?.let { Log.d("Noshairam", it) }
                }.collect {
                    _countryimageslist?.value = Resource.onSuccess(it)
                }
        }
    }
}