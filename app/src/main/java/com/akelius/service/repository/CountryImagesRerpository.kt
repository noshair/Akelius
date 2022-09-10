package com.akelius.service.repository

import android.content.Context
import com.akelius.service.api_service.ApiService
import com.akelius.service.model.countryimagesmodel.ImageDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

class CountryImagesRerpository@Inject constructor(private val apiService: ApiService) {
    fun getTodayNamazData(): Flow<ImageDataClass> = flow {
        emit(apiService.countriesImage())
    }.flowOn(Dispatchers.IO)
}