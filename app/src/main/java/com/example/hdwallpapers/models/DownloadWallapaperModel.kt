package com.example.hdwallpapers.models

import android.app.Application
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hdwallpapers.roomdb.AppDatabase
import com.example.hdwallpapers.roomdb.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DownloadWallapaperModel(application: Application) : AndroidViewModel(application) {

    private val imageDao = AppDatabase.getDatabase(application).imageDao()
    fun getImagesByEmail(email: String): LiveData<List<ImageEntity>> {
        return imageDao.getImagesByEmail(email)
    }

    fun deleteImage(imageEntity: ImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            imageDao.deleteImage(imageEntity)
        }
    }

    fun saveImageToGallery(bitmap: Bitmap, userEmail: String?, onComplete: () -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "HDWallpapers"
                )
                if (!directory.exists()) {
                    directory.mkdir()
                }

                val file = File(directory, "image_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                MediaScannerConnection.scanFile(
                    getApplication(),
                    arrayOf(file.toString()),
                    null,
                    null
                )

                saveImagePathToRoomDB(file.path, userEmail)
                withContext(Dispatchers.Main) {
                    onComplete()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveImagePathToRoomDB(imagePath: String, userEmail: String?) {
        userEmail?.let {
            imageDao.insertImage(ImageEntity(imagePath = imagePath, userEmail = it))
        }


    }
}

