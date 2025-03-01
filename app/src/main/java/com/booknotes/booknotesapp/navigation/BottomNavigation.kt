package com.booknotes.booknotesapp.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MyBottomNavigation(navController: NavHostController) {
    val items = listOf(
        DestinationsBottom.Home,
        DestinationsBottom.Favorites,
        DestinationsBottom.Recommendations,
        DestinationsBottom.Profile
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,//Color.DarkGray,
        contentColor = MaterialTheme.colorScheme.primary,//Color.Black,
        modifier = Modifier.height(50.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    if (currentRoute != screen.route) {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.title,
                            modifier = Modifier.size(25.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            tint = Color(0xFF3295DD),
                            contentDescription = screen.title,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                label = {
                    if (currentRoute != screen.route) {
                        Text(
                            text = screen.title,
                            fontSize = 10.sp
                        )
                    }else{
                        Text(
                            text = screen.title,
                            fontSize = 10.sp,
                            color = Color(0xFF3295DD)
                        )
                    }
                },
                selectedContentColor = Color(0xFF3295DD),
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = false,
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}