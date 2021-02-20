package com.ainsigne.interactivelivestreaming.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream


@Entity(tableName = "streams")
data class Streams(
    @PrimaryKey @ColumnInfo(name = "channel") val channel: String,
    val user: StreamUser, val dateCreated: String, var status: Boolean,
    val imageString: String?, val userId : Int) {
    companion object{
        fun BitmapToBase64(bitmap: Bitmap) : String{
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            return Base64.encodeToString( byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
        }



        fun Base64ToBitmap(imageString: String): Bitmap? {
            val decodedString: ByteArray  = Base64.decode(imageString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }
    }
}