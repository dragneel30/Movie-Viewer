package com.example.myapplication.interfaces

import com.example.myapplication.models.SeatMap
import retrofit2.Call
import retrofit2.http.GET

interface SeatMapService {

    @GET("seatmap.json")
    fun getSeatmap(): Call<SeatMap>


}