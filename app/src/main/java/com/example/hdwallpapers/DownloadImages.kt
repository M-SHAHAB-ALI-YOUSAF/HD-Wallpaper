package com.example.hdwallpapers

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hdwallpapers.adaptor.DownloadImageAdopter
import com.example.hdwallpapers.databinding.ActivityDownloadImagesBinding
import com.example.hdwallpapers.models.DownloadWallapaperModel
import com.example.hdwallpapers.roomdb.ImageEntity
import com.google.firebase.auth.FirebaseAuth

class DownloadImages : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadImagesBinding
    private val downloadViewModel: DownloadWallapaperModel by viewModels()
    private lateinit var adapter: DownloadImageAdopter
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BackButtonOfDownload.setOnClickListener {
            finish()
        }
        setupGridView()
        getCurrentUserEmail()
        fetchImages()
    }

    private fun setupGridView() {
        adapter = DownloadImageAdopter(this, mutableListOf(), downloadViewModel)
        binding.gridViewoffline.adapter = adapter
    }

    private fun getCurrentUserEmail() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        userEmail = currentUser?.email
    }

    private fun fetchImages() {
        userEmail?.let { email ->
            downloadViewModel.getImagesByEmail(email).observe(this) { images ->
                if (images.isEmpty()) {
                    binding.noImagesMessage.visibility = View.VISIBLE
                    binding.gridViewoffline.visibility = View.GONE
                } else {
                    binding.noImagesMessage.visibility = View.GONE
                    binding.gridViewoffline.visibility = View.VISIBLE
                    adapter.updateImageList(images.toMutableList())
                }
            }
        }
    }
}
