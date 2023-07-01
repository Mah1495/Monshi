package com.example.callcenter.screens.contact

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.callcenter.screens.call_screen.UiEvent
import coil.compose.rememberAsyncImagePainter


@Composable
fun AddEditPersonScreen(done: () -> Unit, model: AddEditPersonViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        model.uiEvent.collect {
            when (it) {
                is UiEvent.Done -> done()
                else -> {}
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
        TextField(
            value = model.name,
            onValueChange = {
                model.onEvent(
                    PersonEvent.OnPropertyChanged(
                        AddEditPersonProperty.Name, it
                    )
                )
            },
            placeholder = { Text(text = "Name") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        TextField(
            value = model.number,
            onValueChange = {
                model.onEvent(
                    PersonEvent.OnPropertyChanged(
                        AddEditPersonProperty.Number, it
                    )
                )
            },
            placeholder = { Text(text = "Number") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        TextField(
            value = model.note,
            onValueChange = {
                model.onEvent(PersonEvent.OnPropertyChanged(AddEditPersonProperty.Note, it))
            },
            placeholder = { Text(text = "Note") },
            modifier = Modifier
                .height(128.dp)
                .padding(10.dp)
                .fillMaxWidth()
        )
        Box(modifier = Modifier.weight(1f))
        Button(
            onClick = { model.onEvent(PersonEvent.Add) },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Add", modifier = Modifier.padding(10.dp)
            )
        }
    }
}