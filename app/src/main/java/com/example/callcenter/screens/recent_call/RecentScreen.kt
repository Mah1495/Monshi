package com.example.callcenter.screens.recent_call

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Note
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.callcenter.utils.IDate
import com.example.callcenter.utils.IGroupable
import com.example.callcenter.utils.IIcon
import com.example.callcenter.utils.IImage
import com.example.callcenter.utils.getTime

@Preview
@Composable
fun Iv() {

    Image(
        modifier = Modifier
            .height(100.dp)
            .width(100.dp)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.primary),
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Uri.parse("content://com.android.providers.media.documents/document/image%3A1000000034"))
                .build()
        ),
        contentDescription = ""
    )
}

@Composable
fun GroupedList(
    items: List<IGroupable>,
    expandable: Boolean = true,
    onEvent: (CallsEvent) -> Unit
) {
    val expandIndex = remember { mutableStateOf<Int?>(null) }
    LazyColumn {
        itemsIndexed(items) { index, item ->
            val top = index == 0 || !item.compare(items[index - 1])
            val bottom = index == items.size - 1 || !item.compare(items[index + 1])
            var mod = Modifier.padding(10.dp, 0.dp)
            if (top) {
                mod = mod.clip(shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
            }
            if (bottom) {
                mod = mod.clip(shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
            }
            if (top) {
                // Add a header for each date group
                Text(
                    text = item.groupName(),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.h6,
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = mod
                        .background(Color.LightGray)
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(60.dp)
                            .clickable {
                                if (index == expandIndex.value) {
                                    expandIndex.value = null
                                } else expandIndex.value = index
                            }
                    ) {
                        if (item is IIcon) Icon(item.icon, contentDescription = "")
                        if (item is IImage) {

                            val model = Uri.parse(item.imageUri)
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = Uri.parse(item.imageUri)
                                ),
                                contentDescription = "",
                                Modifier
                                    .width(50.dp)
                                    .height(50.dp)
                                    .clip(CircleShape)
                                    .background(androidx.compose.material3.MaterialTheme.colorScheme.primary),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f),
                            style = MaterialTheme.typography.body1
                        )
                        if (item is IDate) Text(
                            text = item.date.getTime()
                        )
                    }
                    if (expandable && index == expandIndex.value) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(150.dp)
                        ) {
                            RecentCallAction(item.number, FourButtons, onEvent)
                        }
                    }
                }
                if (!bottom) {
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}

val FourButtons = listOf(
    CallButton(Icons.Default.Call, "") { CallsEvent.Call(it) },
    CallButton(Icons.Default.Message, "") { CallsEvent.Message(it) },
    CallButton(Icons.Default.Info, "") { CallsEvent.Info(it) },
    CallButton(Icons.Default.Note, "") { CallsEvent.Notes(it) }
)
val TwoButtons = listOf(
    CallButton(Icons.Default.Call, "") { CallsEvent.Call(it) },
    CallButton(Icons.Default.Message, "") { CallsEvent.Message(it) }
)

data class CallButton(
    val icon: ImageVector,
    val name: String,
    val eventType: (String) -> CallsEvent
)


@Composable
fun RecentCallAction(number: String, buttons: List<CallButton>, onEvent: (CallsEvent) -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        buttons.forEach {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = CenterHorizontally) {
                IconButton(
                    onClick = { onEvent(it.eventType(number)) },
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(CircleShape)
                        .background(Color.Cyan)
                ) {
                    Icon(it.icon, contentDescription = it.name)
                }
            }
        }
    }
}

@Composable
fun RecentScreen(model: RecentCallViewModel = hiltViewModel()) {
    val items = model.logs()
    Column {
        GroupedList(items = items) { model.onEvent(it) }
    }
}


