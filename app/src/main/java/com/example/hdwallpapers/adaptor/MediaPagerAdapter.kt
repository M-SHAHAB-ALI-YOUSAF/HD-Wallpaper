package com.example.hdwallpapers.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.hdwallpapers.R
import com.example.hdwallpapers.SetWallapaper
import com.example.hdwallpapers.VideoPlayer
import com.example.hdwallpapers.retrofit.ImageResult
import com.example.hdwallpapers.retrofit.VideoResult

class MediaPagerAdapter(private var mediaGroups: List<List<MediaItem>>) :
    RecyclerView.Adapter<MediaPagerAdapter.MediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_page, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val mediaGroup = mediaGroups[position]
        holder.bind(mediaGroup)
    }

    override fun getItemCount() = mediaGroups.size

    class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView1: ImageView = view.findViewById(R.id.imageView1)
        private val imageView2: ImageView = view.findViewById(R.id.imageView2)
        private val imageView3: ImageView = view.findViewById(R.id.imageView3)
        private val imageView4: ImageView = view.findViewById(R.id.imageView4)
        private val imageView5: ImageView = view.findViewById(R.id.imageView5)
        private val imageView6: ImageView = view.findViewById(R.id.imageView6)

        fun bind(mediaGroup: List<MediaItem>) {
            val imageViews = listOf(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6)

            for (i in imageViews.indices) {
                val imageView = imageViews[i]
                val mediaItem = mediaGroup.getOrNull(i)

                if (mediaItem != null) {
                    when (mediaItem) {
                        is MediaItem.VideoItem -> {
                            loadImage(imageView, mediaItem.videoResult.thumbnail)
                            imageView.setOnClickListener { openVideoDetail(mediaItem.videoResult.url) }
                        }
                        is MediaItem.ImageItem -> {
                            loadImage(imageView, mediaItem.imageResult.webformatURL)
                            imageView.setOnClickListener { openImageDetail(mediaItem.imageResult.webformatURL) }
                        }
                    }
                    imageView.visibility = View.VISIBLE
                } else {
                    imageView.visibility = View.GONE
                }
            }
        }

        private fun loadImage(imageView: ImageView, url: String?) {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().transform(RoundedCorners(16)))
                .placeholder(R.drawable.img_2)
                .error(R.drawable.baseline_report_gmailerrorred_24)
                .into(imageView)
        }

        private fun openVideoDetail(videoUrl: String) {
            val context = itemView.context
            val intent = Intent(context, VideoPlayer::class.java)
            intent.putExtra("VIDEO_URL", videoUrl)
            context.startActivity(intent)
        }

        private fun openImageDetail(imageUrl: String?) {
            val context = itemView.context
            val intent = Intent(context, SetWallapaper::class.java)
            intent.putExtra("IMAGE_URL", imageUrl)
            context.startActivity(intent)
        }
    }
}
