package com.example.presensiapp.helper

import android.graphics.Bitmap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

fun bitmapToMultipartBodyPart(image: Bitmap, partName: String): MultipartBody.Part {
    val byteArrayOutputStream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream) // Mengompresi gambar
    val byteArray = byteArrayOutputStream.toByteArray()

    val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)
    return MultipartBody.Part.createFormData(partName, "image.jpg", requestBody)
}
