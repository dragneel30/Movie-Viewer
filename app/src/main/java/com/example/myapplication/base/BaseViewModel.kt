package com.example.myapplication.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel() : ViewModel() {

    protected val mIsLoading = MutableLiveData<Boolean>()

    fun getIsLoading(): LiveData<Boolean> {
        return mIsLoading
    }

}