package com.example.hdwallpapers

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.hdwallpapers.databinding.ActivitySetWallapaperBinding
import com.example.hdwallpapers.models.DownloadWallapaperModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.tashila.pleasewait.PleaseWaitDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class SetWallapaper : AppCompatActivity() {

    private lateinit var binding: ActivitySetWallapaperBinding
    private lateinit var wallpaperViewModel: DownloadWallapaperModel
    private lateinit var progressDialog: PleaseWaitDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetWallapaperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = PleaseWaitDialog(context = this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Setting...")


        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val isFromDownload = intent.getBooleanExtra("FROM_DOWNLOAD", false)


        Glide.with(this)
            .load(imageUrl)
            .into(binding.ImageSetToWallpaper)

        if (isFromDownload) {
            binding.downloadCardView.visibility = View.VISIBLE
            binding.OnlineCardView.visibility = View.GONE

            binding.SetWallpaperdownload.setOnClickListener {
                showBottomSheetDialog(imageUrl)
            }
        } else {
            binding.OnlineCardView.visibility = View.VISIBLE
            binding.downloadCardView.visibility = View.GONE

            binding.SetWallpaper.setOnClickListener {
                showBottomSheetDialog(imageUrl)
            }

            binding.DownloadWallpaper.setOnClickListener {
                downloadWallpaper(imageUrl)
            }
        }

        wallpaperViewModel = ViewModelProvider(this).get(DownloadWallapaperModel::class.java)

    }

    private fun downloadWallpaper(imageUrl: String?) {
        if (imageUrl != null) {

            val imageView = binding.ImageSetToWallpaper
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()
            val bitmap: Bitmap = imageView.drawingCache
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

            wallpaperViewModel.saveImageToGallery(bitmap, currentUserEmail) {
                Toast.makeText(applicationContext, "Image Saved!", Toast.LENGTH_SHORT).show()
                binding.DownloadWallpaper.setImageResource(R.drawable.baseline_download_done_24)
                binding.DownloadWallpaper.isEnabled = false

            }
        }
    }


    private fun showBottomSheetDialog(imageUrl: String?) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetBinding =
            com.example.hdwallpapers.databinding.OptionT0SetWallpaperBinding.inflate(
                LayoutInflater.from(this)
            )
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.Home.setOnClickListener {
            setWallpaper(WallpaperManager.FLAG_SYSTEM, imageUrl)
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.Lock.setOnClickListener {
            setWallpaper(WallpaperManager.FLAG_LOCK, imageUrl)
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.Both.setOnClickListener {
            setWallpaper(WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK, imageUrl)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun setWallpaper(flag: Int, imageUrl: String?) {
        progressDialog.show()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    Glide.with(this@SetWallapaper)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get()
                }

                withContext(Dispatchers.IO) {
                    val wallpaperManager = WallpaperManager.getInstance(this@SetWallapaper)
                    wallpaperManager.setBitmap(bitmap, null, true, flag)
                }

                progressDialog.dismiss()
                Toast.makeText(this@SetWallapaper, "Wallpaper Set", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                progressDialog.dismiss()
                Toast.makeText(this@SetWallapaper, "Failed to set wallpaper", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
