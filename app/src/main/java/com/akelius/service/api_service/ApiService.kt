package com.akelius.service.api_service
import com.akelius.service.model.countryimagesmodel.ImageDataClass
import retrofit2.http.*

interface ApiService {
    @GET("assets/")
    suspend fun countriesImage(): ImageDataClass
}