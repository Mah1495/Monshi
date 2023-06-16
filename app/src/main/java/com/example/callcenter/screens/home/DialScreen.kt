package com.example.callcenter.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.callcenter.screens.call_screen.UiEvent

@Composable
fun DialScreen(
    standAlone: Boolean = false,
    model: DialViewModel = hiltViewModel(),
    modifier: Modifier,
    closeSheet: () -> Unit = {}
) {
    var phoneNumber by remember { model.phoneNumber }
    LaunchedEffect(key1 = true) {
        model.uiEvent.collect {
            when (it) {
                is UiEvent.CloseSheet -> {
                    closeSheet()
                }

                else -> {}
            }
        }
    }
    model.sheet.value = standAlone
    Column(modifier = modifier, verticalArrangement = Arrangement.Bottom) {
        CompositionLocalProvider(
            LocalTextInputService provides null
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 25.sp), enabled = !standAlone
                )
                if (!standAlone) {
                    IconButtonLong(onClick = {
                        model.onEvent(DialEvent.Removed)
                    }, onLongClick = {
                        model.onEvent(DialEvent.Cleared)
                    }) {
                        Icon(Icons.Outlined.Backspace, "clear")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        GridNumbers(onEvent = model::onEvent)
        Spacer(modifier = Modifier.height(5.dp))
        if (!standAlone) {
            DialButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                model.onEvent(DialEvent.Called)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IconButtonLong(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 0.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
    }
}

@Composable
fun GridNumbers(onEvent: (DialEvent) -> Unit) {
    val numbers = listOf(
        listOf("1", "2", "3"), listOf("4", "5", "6"), listOf("7", "8", "9"), listOf("*", "0", "#")
    )
    for (i in numbers.indices) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            for (j in 0 until numbers[i].size) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NumberButton(number = numbers[i][j], onEvent)
                }
            }
        }
    }
}

@Composable
fun NumberButton(number: String, onEvent: (DialEvent) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        //Pressed
        onEvent(DialEvent.NumberDown(number))
        //Use if + DisposableEffect to wait for the press action is completed
        DisposableEffect(Unit) {
            onDispose {
                //released
                onEvent(DialEvent.NumberUp(number))
            }
        }
    }
    Button(
        onClick = { },
        interactionSource = interactionSource,
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color.White),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White, contentColor = Color.Transparent
        )
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.h6,
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}


@Composable
fun DialButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .size(78.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary
        ),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
