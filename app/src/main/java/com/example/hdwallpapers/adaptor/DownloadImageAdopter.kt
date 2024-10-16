package com.example.hdwallpapers.adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.hdwallpapers.R
import com.example.hdwallpapers.SetWallapaper
import com.example.hdwallpapers.models.DownloadWallapaperModel
import com.example.hdwallpapers.roomdb.ImageEntity

class DownloadImageAdopter(
    private val context: Context,
    private val imageList: MutableList<ImageEntity>,
    private val downloadViewModel: DownloadWallapaperModel
) : BaseAdapter() {

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Any {
        return imageList[position]
    }

    override fun getItemId(position: Int): Long {
        return imageList[position].id.toLong()
    }

    fun updateImageList(newImages: MutableList<ImageEntity>) {
        imageList.clear()
        imageList.addAll(newImages)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_image, parent, false)

        val imageView: ImageView = view.findViewById(R.id.imageViewdownload)

        val imageEntity = imageList[position]
        Glide.with(context)
            .load(imageEntity.imagePath)
            .placeholder(R.drawable.img_2)
            .error(R.drawable.baseline_report_gmailerrorred_24)
            .into(imageView)


        imageView.setOnClickListener {
            showPopupMenu(view, imageEntity, position)
        }

        return view
    }


    private fun showPopupMenu(view: View, imageEntity: ImageEntity, position: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.image_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    deleteImageWithConfirmation(imageEntity, position)
                    true
                }

                R.id.action_set_wallpaper -> {
                    setWallpaper(imageEntity)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun deleteImageWithConfirmation(imageEntity: ImageEntity, position: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Delete Image")
        builder.setMessage("Are you sure you want to delete this image?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            downloadViewModel.deleteImage(imageEntity)
            imageList.removeAt(position)
            notifyDataSetChanged()
            Toast.makeText(context, "Image Deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }


        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun setWallpaper(imageEntity: ImageEntity) {
        val intent = Intent(context, SetWallapaper::class.java).apply {
            putExtra("IMAGE_URL", imageEntity.imagePath)
            putExtra("FROM_DOWNLOAD", true)
        }
        context.startActivity(intent)
    }
}
