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
import com.example.hdwallpapers.retrofit.VideoResult

class VideoPagerAdapter(private var videoResults: List<List<VideoResult>>) :
    RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_page, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoGroup = videoResults[position]
        holder.bind(videoGroup)
    }

    override fun getItemCount() = videoResults.size

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView1: ImageView = view.findViewById(R.id.imageView1)
        private val imageView2: ImageView = view.findViewById(R.id.imageView2)
        private val imageView3: ImageView = view.findViewById(R.id.imageView3)
        private val imageView4: ImageView = view.findViewById(R.id.imageView4)
        private val imageView5: ImageView = view.findViewById(R.id.imageView5)
        private val imageView6: ImageView = view.findViewById(R.id.imageView6)

        fun bind(videoGroup: List<VideoResult>) {
            val imageViews = listOf(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6)

            for (i in imageViews.indices) {
                val imageView = imageViews[i]
                val videoResult = videoGroup.getOrNull(i)

                if (videoResult != null) {
                    // Load the thumbnail URL into the ImageView
                    Glide.with(imageView.context)
                        .load(videoResult.thumbnail) // Use the thumbnail URL here
                        .apply(RequestOptions().transform(RoundedCorners(16)))
                        .placeholder(R.drawable.img_2)
                        .error(R.drawable.baseline_report_gmailerrorred_24) // Error image
                        .into(imageView)

                    imageView.visibility = View.VISIBLE
                    imageView.setOnClickListener { openVideoDetail(videoResult.url) } // Use the correct video URL
                } else {
                    imageView.visibility = View.GONE
                }
            }
        }

        private fun openVideoDetail(videoUrl: String) {
            val context = itemView.context
            val intent = Intent(context, VideoPlayer::class.java)
            intent.putExtra("VIDEO_URL", videoUrl)
            context.startActivity(intent)
        }
    }
}
