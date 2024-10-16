package com.example.hdwallpapers.retrofit


data class PixabayVideoResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<VideoHit>
)

data class VideoHit(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    val duration: Int,
    val videos: VideoUrls,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val user_id: Int,
    val user: String,
    val userImageURL: String
)

data class VideoUrls(
    val large: VideoData,
    val medium: VideoData,
    val small: VideoData,
    val tiny: VideoData
)

data class VideoData(
    val url: String,
    val thumbnail: String
)

data class VideoResult(
    val url: String,
    val thumbnail: String
)
