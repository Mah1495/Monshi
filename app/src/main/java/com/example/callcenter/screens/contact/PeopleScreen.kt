package com.example.callcenter.screens.contact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.callcenter.screens.recent_call.GroupedList
import kotlinx.coroutines.flow.collect


@Composable
fun PeopleScreen(model: PeopleViewModel = hiltViewModel()) {
    val items = model.contacts.collectAsState(initial = emptyList())
    GroupedList(items = items.value) {
        model.onEvent(it)
    }
}