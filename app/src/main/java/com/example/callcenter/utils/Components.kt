package com.example.callcenter.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    IconButton(
        onClick = { activityResultLauncher.launch("image/*") },
        modifier = Modifier
            .padding(10.dp)
            .clip(CircleShape)
    ) {
        Icon(Icons.Default.PhotoCamera, "Select Image")
    }
}
