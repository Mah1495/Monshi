package ir.vvin.monshi.screens.call_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CallScreen(
    openSheet: () -> Unit = {},
    model: CallScreenViewModel = hiltViewModel(),
    keypadState: MutableState<Boolean>? = null
) {
    model.keypad.value = keypadState?.value ?: false
    LaunchedEffect(key1 = true) {
        model.uiEvent.collect {
            when (it) {
                is UiEvent.OpenSheet -> {
                    openSheet()
                }

                else -> {}
            }
        }
    }
    Column {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = model.callHandler.callInfo!!.displayName())
            Text(text = model.callHandler.callInfo!!.number!!)
            model.callHandler.callInfo?.note?.let {
                Text(text = model.callHandler.callInfo?.note ?: "")
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                CallButton(
                    label = "Mute",
                    icon = Icons.Default.MicOff, model.muted.value,
                    { model.onEvent(CallEvent.Mute) })
                CallButton(
                    label = "Keypad",
                    icon = Icons.Default.Dialpad, model.keypad.value,
                    { model.onEvent(CallEvent.Keypad) })
                CallButton(
                    label = "Speaker",
                    icon = Icons.Default.VolumeUp, model.speaker.value,
                    { model.onEvent(CallEvent.Speaker) })
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                CallButton(label = "Add Call", icon = Icons.Default.Add, false, {})
                CallButton(label = "Video Call", icon = Icons.Default.VideoCall, false, {})
                CallButton(label = "Hold", icon = Icons.Default.Pause, false, {})
            }
            Spacer(modifier = Modifier.height(10.dp))
            IconButton(
                onClick = { model.onEvent(CallEvent.Disconnect) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Red)
            ) {
                Icon(Icons.Default.CallEnd, "End")
            }
        }
    }
}

@Composable
fun CallButton(
    label: String, icon: ImageVector, enabled: Boolean,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(color = if (enabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer) {
        Box(
            modifier = modifier
                .padding(20.dp)
                .size(80.dp), contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onClick, modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxWidth()
            ) {
                Icon(
                    icon, label,
                    modifier
                        .size(48.dp)
                        .padding(10.dp)
                )
            }
            Text(text = label, modifier = Modifier.align(Alignment.BottomCenter), fontSize = 10.sp)
        }
    }
}