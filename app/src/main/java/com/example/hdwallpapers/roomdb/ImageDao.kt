package com.example.hdwallpapers.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    fun insertImage(imageEntity: ImageEntity)


    @Query("SELECT * FROM images WHERE userEmail = :email")
    fun getImagesByEmail(email: String): LiveData<List<ImageEntity>>

    @Query("DELETE FROM images WHERE id = :id")
    fun deleteImageById(id: Int)

    @Delete
    fun deleteImage(image: ImageEntity)
}
