package com.example.hdwallpapers.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hdwallpapers.retrofit.PixabayVideoResponse
import com.example.hdwallpapers.retrofit.RetrofitClient
import com.example.hdwallpapers.retrofit.VideoResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoViewModel : ViewModel() {
    private val _videoList = MutableLiveData<List<VideoResult>>()
    val videoList: LiveData<List<VideoResult>> get() = _videoList

    fun fetchVideos(apiKey: String) {
        RetrofitClient.pixabayVideoApi.getVideos(apiKey, 200, "popular", "animals")
            .enqueue(object : Callback<PixabayVideoResponse> {
                override fun onResponse(
                    call: Call<PixabayVideoResponse>,
                    response: Response<PixabayVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        _videoList.value = response.body()?.hits?.map { hit ->
                            VideoResult(
                                url = hit.videos.large.url,
                                thumbnail = hit.videos.medium.thumbnail
                            )
                        } ?: emptyList()
                    } else {
                        _videoList.value = emptyList()
                    }
                }

                override fun onFailure(call: Call<PixabayVideoResponse>, t: Throwable) {
                    _videoList.value = emptyList()
                }
            })
    }
}
