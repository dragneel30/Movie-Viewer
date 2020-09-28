package com.example.myapplication.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.NetworkingService
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.models.Movie
import com.example.myapplication.utils.TimeUtils.numToDuration

class DetailsViewModel() : BaseViewModel() {

    private val mData = MutableLiveData<Movie>()

    fun getData(): LiveData<Movie>{
        return mData
    }

    init {
        fecthMovie()
    }


    fun fecthMovie() {
        mIsLoading.value = true

        NetworkingService.fetchMovie( object: NetworkingService.ServiceAPICallback<Movie> {
            override fun onSuccess(responseData: Movie?) {

                if (responseData?.runtime_mins != null) {
                    responseData?.runtime_mins = numToDuration(responseData?.runtime_mins)
                }

                mData.postValue(responseData)
                mIsLoading.postValue(false)
            }

            override fun onFailure() {
                mData.postValue(null)
                mIsLoading.postValue(false)
            }
        })

    }
}