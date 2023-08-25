package ir.vvin.monshi.screens.incoming_call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun IncomingCallScreen(
    model: IncomingCallViewModel = hiltViewModel(),
    accept: () -> Unit,
    reject: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            model.callHandler.callInfo?.let {
                Text(text = it.displayName())
                Text(text = it.number ?: "")
                Text(text = it.note ?: "")
            }
        }
        Row(Modifier.weight(1f)) {
            Box {}
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Column(
                Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { accept() },
                    Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                ) {
                    Icon(Icons.Default.Call, "")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Accept")
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { reject() },
                    Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = "")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Decline")
            }
        }
    }
}
