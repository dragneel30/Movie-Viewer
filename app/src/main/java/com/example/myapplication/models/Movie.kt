package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Movie (
    val advisory_rating: String,
    val canonical_title: String,
    val genre: String,
    val link_name: String,
    val poster: String,
    val poster_landscape: String,
    val theater: String,
    val trailer: String,
    val synopsis: String,
    var runtime_mins: String,
    val release_date: String,
    val is_showing: Int,
    val average_rating: Float,
    val total_reviews: Int,
    val is_featured: Int,
    val watch_list: Boolean,
    val your_rating: Int,
    val is_inactive: Int,
    val has_schedules: Int,
    @SerializedName("cast")
    val casts: ArrayList<String>
)