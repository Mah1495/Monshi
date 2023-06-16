package com.example.callcenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.primarySurface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.callcenter.screens.home.DialScreen
import com.example.callcenter.screens.contact.PeopleScreen
import com.example.callcenter.screens.recent_call.RecentScreen

@Composable
fun MainScreenView(showActivity: () -> Unit) {
    val navController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val show = navBackStackEntry.value?.destination?.route == "people"
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }, topBar = {
        if (show) {
            TopAppBar(title = {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { showActivity() }) {
                        Icon(Icons.Default.Add, contentDescription = "add")
                    }
                }
            }, backgroundColor = MaterialTheme.colors.background)
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
        backgroundColor = Color.LightGray, contentColor = Color.Black
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.value?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(icon = {
                Icon(
                    item.icon,
                    contentDescription = item.title
                )
            },
                label = {
                    Text(
                        text = item.title, fontSize = 9.sp
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
    object Recent :
        BottomNavItem("Recent", "recent", Icons.Default.History)

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