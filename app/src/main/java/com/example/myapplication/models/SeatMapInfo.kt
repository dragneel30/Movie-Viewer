package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

   data class SeatMapInfo (
   @SerializedName("seats")
   val available_seats: ArrayList<String>,
   val seat_count: Int
)