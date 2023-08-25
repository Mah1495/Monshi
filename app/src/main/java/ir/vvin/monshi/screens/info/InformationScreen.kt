@file:OptIn(ExperimentalMaterial3Api::class)

package ir.vvin.monshi.screens.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.vvin.monshi.screens.recent_call.GroupedList
import ir.vvin.monshi.screens.recent_call.RecentCallAction
import ir.vvin.monshi.screens.recent_call.TwoButtons

private sealed class InformationRoute(val route: String, val icon: ImageVector) {
    object Notes : InformationRoute("Notes", Icons.Default.Notes)
    object History : InformationRoute("History", Icons.Default.History)
}

private val navs = listOf(InformationRoute.History, InformationRoute.Notes)

@Composable
fun informationScreen(number: String, model: InformationViewModel = hiltViewModel()) {
    val controller = rememberNavController()
    number.also { model.number = it }
    Column {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.outlineVariant)
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            Text(text = model.number)
            RecentCallAction(model.number, TwoButtons, onEvent = model::onEvent)
        }
        Column(modifier = Modifier.weight(1f)) {
            Scaffold(bottomBar = { bottom(controller = controller) }) {
                Box(modifier = Modifier.padding(it)) {
                    NavHost(
                        navController = controller,
                        startDestination = InformationRoute.History.route
                    ) {
                        composable(InformationRoute.History.route) {
                            historyScreen(model.number)
                        }
                        composable(InformationRoute.Notes.route) {
                            notesScreen(model.number)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun bottom(controller: NavController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {

        navs.forEach {
            BottomNavigationItem(icon = {
                Icon(
                    imageVector = it.icon,
                    contentDescription = it.route
                )
            }, label = {
                Text(
                    text = it.route
                )
            }, onClick = {
                controller.navigate(it.route) {
                    controller.graph.startDestinationRoute?.let {
                        popUpTo(it) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }, selected = controller.currentBackStackEntry?.destination?.route == it.route)
        }
    }
}

@Composable
fun notesScreen(number: String, model: NoteViewModel = hiltViewModel()) {
    val list = model.notes(number.trim()).collectAsState(initial = emptyList())

    LazyColumn(modifier = Modifier.padding(10.dp, 0.dp)) {
        items(list.value) {
            Text(text = it.note)
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun historyScreen(number: String, model: HistoryViewModel = hiltViewModel()) {
    val list = model.logs(number)
    GroupedList(items = list, false, onEvent = {})
}
