package com.booknotes.booknotesapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.booknotes.booknotesapp.ui.screens.FavoritesScreen
import com.booknotes.booknotesapp.ui.screens.home.HomeScreen
import com.booknotes.booknotesapp.ui.screens.info.InformationScreen
import com.booknotes.booknotesapp.ui.screens.ProfileScreen
import com.booknotes.booknotesapp.ui.screens.RecommendationsScreen

@Composable
fun NavigationGraphBottom(navController: NavHostController) {
    NavHost(navController = navController, startDestination = DestinationsBottom.Home.route) {
        composable(DestinationsBottom.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(DestinationsBottom.Favorites.route) {
            FavoritesScreen()
        }
        composable(DestinationsBottom.Recommendations.route) {
            RecommendationsScreen()
        }
        composable(DestinationsBottom.Profile.route) {
            ProfileScreen()
        }
        composable(
            DestinationsBottom.Information.route + "/{${DestinationsBottom.Information.argBookId}}",
            arguments = listOf(
                navArgument(DestinationsBottom.Information.argBookId)
                { type = NavType.StringType })
        ) {
            InformationScreen(
                Modifier,
                it
                    .arguments
                    ?.getString(DestinationsBottom.Information.argBookId)
            )
        }
    }
}