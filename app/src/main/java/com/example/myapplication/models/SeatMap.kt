package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class SeatMap (
    val seatmap: ArrayList<ArrayList<String>>,
    @SerializedName("available")
    val info: SeatMapInfo
)