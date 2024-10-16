package com.example.hdwallpapers.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hdwallpapers.retrofit.ImageResult
import com.example.hdwallpapers.retrofit.PixabayResponse
import com.example.hdwallpapers.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageViewModel : ViewModel() {
    private val _images = MutableLiveData<List<ImageResult>>()
    val images: LiveData<List<ImageResult>> get() = _images


    fun fetchImages(apiKey: String) {

        RetrofitInstance.api.getImages(apiKey, 100, "popular")
            .enqueue(object : Callback<PixabayResponse> {
                override fun onResponse(
                    call: Call<PixabayResponse>,
                    response: Response<PixabayResponse>
                ) {
                    if (response.isSuccessful) {
                        _images.value = response.body()?.hits ?: emptyList()
                    }

                }

                override fun onFailure(call: Call<PixabayResponse>, t: Throwable) {
                    _images.value = emptyList()

                }
            })
    }
}
