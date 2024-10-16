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
import com.example.hdwallpapers.retrofit.ImageResult
import com.example.hdwallpapers.SetWallapaper

class ImagePagerAdapter(private var images: List<List<ImageResult>>) :
    RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_page, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageGroup = images[position]
        holder.bind(imageGroup)
    }

    override fun getItemCount() = images.size

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView1: ImageView = view.findViewById(R.id.imageView1)
        private val imageView2: ImageView = view.findViewById(R.id.imageView2)
        private val imageView3: ImageView = view.findViewById(R.id.imageView3)
        private val imageView4: ImageView = view.findViewById(R.id.imageView4)
        private val imageView5: ImageView = view.findViewById(R.id.imageView5)
        private val imageView6: ImageView = view.findViewById(R.id.imageView6)

        fun bind(imageGroup: List<ImageResult>) {
            val imageViews =
                listOf(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6)

            for (i in imageViews.indices) {
                val imageView = imageViews[i]
                val imageUrl = imageGroup.getOrNull(i)?.webformatURL

                if (imageUrl != null) {
                    Glide.with(imageView.context)
                        .load(imageUrl)
                        .apply(RequestOptions().transform(RoundedCorners(16)))
                        .placeholder(R.drawable.img_2)
                        .error(R.drawable.baseline_report_gmailerrorred_24)
                        .into(imageView)

                    imageView.visibility = View.VISIBLE
                    imageView.setOnClickListener { openImageDetail(imageUrl) }
                } else {
                    imageView.visibility = View.GONE
                }
            }
        }

        private fun openImageDetail(imageUrl: String?) {
            val context = itemView.context
            val intent = Intent(context, SetWallapaper::class.java)
            intent.putExtra("IMAGE_URL", imageUrl)
            context.startActivity(intent)
        }
    }
}
