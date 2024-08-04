package com.raman.project.openinapp.repositories

import android.util.Log
import com.google.gson.Gson
import com.raman.project.openinapp.api.ApiService
import com.raman.project.openinapp.api.RetrofitClient
import com.raman.project.openinapp.models.ApiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LinkRepository @Inject constructor() {

//    private val apiService: ApiService
//
//    init {
//        val retrofit = RetrofitClient.getInstance()
//        apiService = retrofit.create(ApiService::class.java)
//    }
//
//    fun getDataFromApi(): ApiData {
//        val response = RetrofitClient.getInstance().create(ApiService::class.java).getApiData()
//
//        return response
//
////        if(response.isSuccessful){
////            return response.body()
////        }
////        else{
////            Log.e("API Error", "Error : ${response.code()}")
////            return null
////        }
//
//
//    }
suspend fun getDataFromApi(): ApiData? {
    return withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.getInstance().create(ApiService::class.java).getApiData().await()


            if (response.isSuccessful) {
                Log.e("TAG1","Response  successful: ${response.body()}")
                response.body()

            } else {
                // Log the response code and message for debugging
                Log.e("TAG","Response not successful: ${response.code()} - ${response.message()}")

                null
            }
        } catch (e: Exception) {
            // Log the exception for debugging
            e.printStackTrace()
            null
        }
    }
}

}