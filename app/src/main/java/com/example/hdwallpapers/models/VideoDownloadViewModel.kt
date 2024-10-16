package com.example.hdwallpapers.models

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter

class VideoDownloadViewModel(application: Application) : AndroidViewModel(application) {

    private val _downloadStatus = MutableLiveData<String>()
    val downloadStatus: LiveData<String> get() = _downloadStatus

    private var downloadId: Long = 0L
    private lateinit var downloadManager: DownloadManager

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor: Cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    if (statusIndex != -1) {
                        val status = cursor.getInt(statusIndex)
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            _downloadStatus.postValue("Video downloaded successfully!")
                        } else if (status == DownloadManager.STATUS_FAILED) {
                            _downloadStatus.postValue("Download failed!")
                        }
                    } else {
                        _downloadStatus.postValue("Error: Could not fetch download status.")
                    }
                }
                cursor.close()
            }
        }
    }

    init {
        getApplication<Application>().registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    fun downloadVideo(videoUrl: String) {
        downloadManager = getApplication<Application>().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(videoUrl))
            .setTitle("Downloading Video")
            .setDescription("Please wait...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "video_${System.currentTimeMillis()}.mp4")

        downloadId = downloadManager.enqueue(request)
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(onDownloadComplete)
    }
}
