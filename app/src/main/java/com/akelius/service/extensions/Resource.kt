package com.akelius.service.extensions

import retrofit2.Call
import retrofit2.Response

sealed class Resource<T>(val data: T? = null, val error: String? = null) {
     class onSuccess<T>(data: T?) : Resource<T>(data)
     class onFailure<T>(error: T? = null) : Resource<T>(error)
     class onLoading<T>() : Resource<T>()
}
