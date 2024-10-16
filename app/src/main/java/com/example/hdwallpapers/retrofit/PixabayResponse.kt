package com.example.hdwallpapers.retrofit

data class PixabayResponse(
    val hits: List<ImageResult>
)

data class ImageResult(
    val webformatURL: String
)