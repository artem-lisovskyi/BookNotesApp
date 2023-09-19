package com.booknotes.booknotesapp.ui.screens.info

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.booknotes.booknotesapp.ui.screens.home.MyTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(modifier: Modifier = Modifier, book: String?) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MyTopAppBar() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) { }
    }
}