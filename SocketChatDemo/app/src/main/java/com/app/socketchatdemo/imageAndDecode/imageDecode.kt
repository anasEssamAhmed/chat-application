package com.app.socketchatdemo.imageAndDecode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class imageDecode {

    fun imageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return Base64.encodeToString(
            byteArrayOutputStream.toByteArray(),
            Base64.DEFAULT
        )
    }

    fun decodeImage(data: String): Bitmap? {
        val decodedString: ByteArray = Base64.decode(data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}