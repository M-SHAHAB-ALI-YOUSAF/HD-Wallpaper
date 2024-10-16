package com.example.hdwallpapers.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.nio.ByteOrder

interface PixabayApi {
    @GET("/api/")
    fun getImages(
        @Query("key") apiKey: String,
        @Query("per_page") perPage: Int,
        @Query("order") order: String,
    ): Call<PixabayResponse>
}