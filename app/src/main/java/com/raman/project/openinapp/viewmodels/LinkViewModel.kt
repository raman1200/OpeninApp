package com.raman.project.openinapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raman.project.openinapp.models.ApiData
import com.raman.project.openinapp.repositories.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkViewModel @Inject constructor(private val repository: LinkRepository) : ViewModel() {

    private val _apiData = MutableLiveData<ApiData?>()
    val apiData: LiveData<ApiData?> = _apiData

    fun getData() {


        viewModelScope.launch {
            val ad = repository.getDataFromApi()

            if (ad == null) {
                // Update UI or do something with the data
                Log.e("EEEERRRROOOORRR", ad.toString())
            } else {
                _apiData.postValue(ad)
                // Handle the error case
            }
        }


    }


}