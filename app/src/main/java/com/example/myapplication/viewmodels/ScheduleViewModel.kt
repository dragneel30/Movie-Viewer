package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.NetworkingService
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.models.*

class ScheduleViewModel() : BaseViewModel() {


    private val mData = MutableLiveData<Schedule>()
    private val mCinemas = MutableLiveData<ArrayList<Cinema>>()
    private val mDates = MutableLiveData<ArrayList<Date>>()
    private val mTimes = MutableLiveData<ArrayList<Time>>()

    private val mCurrentPrice = MutableLiveData<String>()

    private var lastSelectedCinemaIndex = 0
    private var lastSelectedDateIndex = 0
    private var lastSelectedTimeIndex = 0

    init {
        fecthSchedule()
    }

    fun getCinemas(): LiveData<ArrayList<Cinema>> {
        return mCinemas
    }

    fun getCurrentPrice(): LiveData<String> {
        return mCurrentPrice
    }


    fun getTimes(): LiveData<ArrayList<Time>> {
        return mTimes
    }


    fun getData(): LiveData<Schedule>{
        return mData
    }

    fun changeDate(item: Date, index: Int) {

        resetCinema()
        mData.value?.dates!![lastSelectedDateIndex].clicked = false
        item.clicked = true
        lastSelectedDateIndex = index
        val cinemas = mData.value?.cinemas
        if ( cinemas != null ) {
            cinemas.forEachIndexed{i, cinema ->
                if ( cinema.parent == item.id ) {
                    mCinemas.value = cinema.cinemas
                    // reset to first option
                    changeCinema(cinema.cinemas[0], 0)
                    return
                }
            }
            mCinemas.value = null
            mTimes.value = null
            mCurrentPrice.value = null
        }

    }


    fun changeCinema(item: Cinema, index: Int) {

        resetTime()
        mCinemas.value!![lastSelectedCinemaIndex].clicked = false
        item.clicked = true
        val times = mData.value?.times
        lastSelectedCinemaIndex = index
        if ( times != null ) {
            times.forEachIndexed {i, time ->
                if ( time.parent == item.id ) {
                    mTimes.value = time.times
                    changeTime(time.times[0], 0)
                    return
                }

            }
            mTimes.value = null
            mCurrentPrice.value = null
        }
    }

    fun changeTime(item: Time, index: Int) {

        mTimes.value!![lastSelectedTimeIndex].clicked = false
        item.clicked = true
        mCurrentPrice.value = item.price
        lastSelectedTimeIndex = index

    }

    fun resetCinema() {
        if ( mCinemas.value != null ) {
            mCinemas.value!![lastSelectedCinemaIndex].clicked = false
            lastSelectedCinemaIndex = 0
        }
    }
    fun resetTime() {
        if ( mTimes.value != null ) {
            mTimes.value!![lastSelectedTimeIndex].clicked = false
            lastSelectedTimeIndex = 0
        }
    }

    fun fecthSchedule() {
        mIsLoading.value = true

        NetworkingService.getSchedule(object: NetworkingService.ServiceAPICallback<Schedule> {
            override fun onSuccess(responseData: Schedule?) {

                mData.value = responseData
                mDates.value = responseData?.dates

                if ( responseData?.cinemas != null && responseData?.cinemas.size > 0 && responseData?.cinemas[0].cinemas.size > 0 ) {
                    responseData?.cinemas[0].cinemas[0].clicked = true
                    mCinemas.value = responseData?.cinemas[0].cinemas
                }
                if ( responseData?.times != null && responseData?.times.size > 0 && responseData?.times[0].times.size > 0 ) {
                    responseData?.times[0].times[0].clicked = true
                    mTimes.value = responseData?.times[0].times
                    mCurrentPrice.value = responseData?.times[0].times[0].price
                }
                if ( responseData?.dates != null && responseData?.dates.size > 0 ) {
                    responseData?.dates[0].clicked = true
                }

                mIsLoading.value = false
            }

            override fun onFailure() {
                mData.postValue(null)
                mCinemas.postValue(null)
                mTimes.postValue(null)
                mIsLoading.postValue(false)
            }
        })

    }

}