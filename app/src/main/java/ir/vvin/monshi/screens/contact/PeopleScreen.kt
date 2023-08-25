package ir.vvin.monshi.screens.contact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import ir.vvin.monshi.screens.recent_call.GroupedList


@Composable
fun PeopleScreen(model: PeopleViewModel = hiltViewModel()) {
    val items = model.contacts.collectAsState(initial = emptyList())
    GroupedList(items = items.value) {
        model.onEvent(it)
    }
}