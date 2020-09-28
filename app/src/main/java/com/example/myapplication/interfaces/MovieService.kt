package com.example.myapplication.interfaces

import com.example.myapplication.models.Movie
import retrofit2.Call
import retrofit2.http.GET;
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query;

interface MovieService {

    @GET("movie.json")
    fun getMovie(): Call<Movie>


}