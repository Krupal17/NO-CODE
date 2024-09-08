package com.kp.softsavvy.futeres.no_code.false_coding

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.InputStream

fun getBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        // Open the input stream from the URI
        val inputStream: InputStream? = contentResolver.openInputStream(uri)

        // Decode the input stream into a Bitmap
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Close the input stream
        inputStream?.close()

        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}