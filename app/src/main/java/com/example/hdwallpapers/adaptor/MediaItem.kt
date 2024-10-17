package com.example.hdwallpapers.adaptor

import com.example.hdwallpapers.retrofit.ImageResult
import com.example.hdwallpapers.retrofit.VideoResult

sealed class MediaItem {
    data class VideoItem(val videoResult: VideoResult) : MediaItem()
    data class ImageItem(val imageResult: ImageResult) : MediaItem()
}
