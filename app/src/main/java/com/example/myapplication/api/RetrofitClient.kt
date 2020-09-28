package com.example.myapplication.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val mClient = OkHttpClient.Builder().build()
    private val mRetrofit = Retrofit.Builder()
        .baseUrl(NetworkingService.baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(mClient)
        .build()

    fun<T> build(service: Class<T>): T{
        return mRetrofit.create(service)
    }

}