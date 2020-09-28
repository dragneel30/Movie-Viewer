package com.example.myapplication.api

import com.example.myapplication.interfaces.MovieService
import com.example.myapplication.interfaces.ScheduleService
import com.example.myapplication.interfaces.SeatMapService
import com.example.myapplication.models.Movie
import com.example.myapplication.models.Schedule
import com.example.myapplication.models.SeatMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NetworkingService {

    const val baseURL = "http://ec2-52-76-75-52.ap-southeast-1.compute.amazonaws.com/"

    interface ServiceAPICallback<T> {
        fun onSuccess(responseData: T?)
        fun onFailure()
    }

    fun fetchMovie(callback: ServiceAPICallback<Movie>) {

        val request = RetrofitClient.build(MovieService::class.java)

        val call = request.getMovie()

        call.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {

                callback.onSuccess(response.body())

            }
            override fun onFailure(call: Call<Movie>, t: Throwable) {

                callback.onFailure()

            }

        })

    }

    fun getSeatMap(callback: ServiceAPICallback<SeatMap>) {

        val request = RetrofitClient.build(SeatMapService::class.java)

        val call = request.getSeatmap()

        call.enqueue(object : Callback<SeatMap> {

            override fun onResponse(call: Call<SeatMap>, response: Response<SeatMap>) {

                callback.onSuccess(response.body())


            }
            override fun onFailure(call: Call<SeatMap>, t: Throwable) {

                t.printStackTrace()
                callback.onFailure()

            }

        })

    }

    fun getSchedule(callback: ServiceAPICallback<Schedule>) {

        val request = RetrofitClient.build(ScheduleService::class.java)

        val call = request.getSchedule()

        call.enqueue(object : Callback<Schedule> {

            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {

                callback.onSuccess(response.body())


            }
            override fun onFailure(call: Call<Schedule>, t: Throwable) {

                t.printStackTrace()
                callback.onFailure()

            }

        })

    }


}