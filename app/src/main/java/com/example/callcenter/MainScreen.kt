package com.example.callcenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.callcenter.screens.contact.PeopleScreen
import com.example.callcenter.screens.home.DialScreen
import com.example.callcenter.screens.recent_call.RecentScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenView(showActivity: (Int) -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val show = navBackStackEntry.value?.destination?.route == "people"
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }, topBar = {
        if (show) {
            TopAppBar(title = {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = {
                        showActivity(0)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "add")
                    }
                }
            })
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Phone, BottomNavItem.Recent, BottomNavItem.People
    )
    BottomNavigation(
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.value?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(icon = {
                Icon(
                    item.icon,
                    contentDescription = item.title,
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
            },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}

sealed class BottomNavItem(var title: String, var screen_route: String, var icon: ImageVector) {
    object Phone : BottomNavItem("Phone", "phone", Icons.Default.Call)
    object Recent : BottomNavItem("Recent", "recent", Icons.Default.History)

    object People : BottomNavItem("People", "people", Icons.Default.People)
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.Phone.screen_route) {
        composable(BottomNavItem.Phone.screen_route) {

            DialScreen(modifier = Modifier.fillMaxSize())

        }
        composable(BottomNavItem.Recent.screen_route) {
            RecentScreen()
        }
        composable(BottomNavItem.People.screen_route) {
            PeopleScreen()
        }
    }
}
