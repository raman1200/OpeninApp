package com.raman.project.openinapp.api

import com.raman.project.openinapp.models.ApiData
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
//    https://api.inopenapp.com/api/v1/dashboardNew
    @GET("/api/v1/dashboardNew")
    fun getApiData(): Deferred<Response<ApiData>>
}