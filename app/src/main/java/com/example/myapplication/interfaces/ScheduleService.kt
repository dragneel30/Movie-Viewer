package com.example.myapplication.interfaces

import com.example.myapplication.models.Movie
import com.example.myapplication.models.Schedule
import retrofit2.Call
import retrofit2.http.GET



interface ScheduleService {

    @GET("schedule.json")
    fun getSchedule(): Call<Schedule>


}