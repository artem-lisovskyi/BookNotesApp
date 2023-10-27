package com.booknotes.booknotesapp.ui.screens.favourites

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.booknotes.booknotesapp.ui.MyTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController:NavHostController, modifier: Modifier = Modifier, bottomNav: @Composable () -> Unit) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MyTopAppBar() },
        bottomBar = bottomNav//{ MyBottomNavigation(navController = navController) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {  }
    }
}