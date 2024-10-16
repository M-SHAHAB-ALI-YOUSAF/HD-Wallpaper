package com.example.hdwallpapers.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayVideoApi {
    @GET("/api/videos/")
    fun getVideos(
        @Query("key") apiKey: String,
        @Query("per_page") perPage: Int,
        @Query("order") order: String,
        @Query("category") category: String,

    ): Call<PixabayVideoResponse>
}
