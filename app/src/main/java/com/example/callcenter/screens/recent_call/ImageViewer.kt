package com.example.callcenter.screens.recent_call

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext

@Composable
fun ImageViewer(imageFilePath: String) {
    val options = BitmapFactory.Options()
    options.inPreferredConfig = Bitmap.Config.ARGB_8888
    options.inSampleSize = 2
    val stream = LocalContext.current.contentResolver.openInputStream(Uri.parse(imageFilePath))
    val bitmap = BitmapFactory.decodeStream(stream)
    val painter: Painter = BitmapPainter(bitmap.asImageBitmap())
    Image(
        painter = painter,
        contentDescription = "Local Image",
        modifier = Modifier.fillMaxSize()
    )
}

fun Bitmap.resizeBitmap(newWidth: Int, newHeight: Int): Bitmap {
    val scaledBitmap = Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
    this.recycle() // Recycle the original bitmap if no longer needed
    return scaledBitmap
}