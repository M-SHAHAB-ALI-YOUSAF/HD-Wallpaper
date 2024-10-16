package com.example.hdwallpapers

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hdwallpapers.databinding.ActivityVideoPlayerBinding
import com.example.hdwallpapers.models.VideoDownloadViewModel

class VideoPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private val videoDownloadViewModel: VideoDownloadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val videoUrl = intent.getStringExtra("VIDEO_URL")
        videoDownloadViewModel.downloadStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }

        if (videoUrl != null) {
            playVideo(videoUrl)
        }


        binding.DownloadVideo.setOnClickListener {
            val videoUrl = intent.getStringExtra("VIDEO_URL")
            if (videoUrl != null) {
                videoDownloadViewModel.downloadVideo(videoUrl)
            }
        }
    }

    private fun playVideo(videoUrl: String) {

        val uri: Uri = Uri.parse(videoUrl)


        binding.VideoPlayer.setVideoURI(uri)


//        val mediaController = MediaController(this)
//        mediaController.setAnchorView(binding.VideoPlayer)
//        binding.VideoPlayer.setMediaController(mediaController)


        binding.VideoPlayer.start()


        binding.progressBar.visibility = android.view.View.VISIBLE

        binding.VideoPlayer.setOnPreparedListener {

            binding.progressBar.visibility = android.view.View.GONE
        }

        binding.VideoPlayer.setOnErrorListener { _, _, _ ->
            binding.progressBar.visibility = android.view.View.GONE
            return@setOnErrorListener true
        }
    }

    override fun onPause() {
        super.onPause()

        binding.VideoPlayer.pause()
    }

    override fun onResume() {
        super.onResume()

        binding.VideoPlayer.start()
    }
}
