package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.api.NetworkingService
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.models.SelectedSeat
import com.example.myapplication.models.SeatMap
import kotlin.collections.ArrayList

class SeatViewModel() : BaseViewModel() {


    private val mData = MutableLiveData<SeatMap>()
    private val mTotalPrice = MutableLiveData<Float>()
    private val mSelectedSeats = MutableLiveData<ArrayList<SelectedSeat>>()

    init {
        fetchSeatmap()
    }

    fun getSelectedSeats(): LiveData<ArrayList<SelectedSeat>> {
        return mSelectedSeats
    }

    fun getTotalPrice(): LiveData<Float> {
        return mTotalPrice
    }


    fun getData(): LiveData<SeatMap>{
        return mData
    }

    fun findSelectedSeat(name: String): SelectedSeat? {
        mSelectedSeats.value?.forEach { selectedSeat ->
            if ( selectedSeat.seat_name == name ) {
                return selectedSeat
            }
        }
        return null
    }

    fun selectSeats(price: String?, name: String): Boolean {
        if ( price == null ) return false
        val selectedSeats = mSelectedSeats.value
        var totalPrice = mTotalPrice.value
        val selectedSeat = findSelectedSeat(name)
        /// deselect seat
        if ( selectedSeat != null ) {
            selectedSeats?.remove(selectedSeat)
            if (totalPrice != null) {
                totalPrice -= selectedSeat.price.toFloat()
            }
            mSelectedSeats.value = selectedSeats
            mTotalPrice.value = totalPrice
            return false
        }
        val availableSeats = mData.value?.info?.available_seats
        val seatIndex = availableSeats?.indexOf(name)
         // select seat
        if ( seatIndex != -1 ) {
            if (totalPrice != null) {
                totalPrice += price.toFloat()
            }
            selectedSeats?.add(SelectedSeat(price, name))
            mSelectedSeats.value = selectedSeats
            mTotalPrice.value = totalPrice

            return true
        }
        return false
    }

    fun fetchSeatmap() {
        mIsLoading.value = true

        mTotalPrice.value = 0f
        NetworkingService.getSeatMap(object: NetworkingService.ServiceAPICallback<SeatMap> {
            override fun onSuccess(responseData: SeatMap?) {
                mSelectedSeats.value = ArrayList()
                mData.postValue(responseData)
                mIsLoading.postValue(false)
            }

            override fun onFailure() {
                mSelectedSeats.value = ArrayList()
                mData.postValue(null)
                mIsLoading.postValue(false)
            }
        })

    }
}