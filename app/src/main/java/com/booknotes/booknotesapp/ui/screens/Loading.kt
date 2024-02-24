package com.booknotes.booknotesapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.booknotesapp.booknotesapp.R

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, loadingText: String = "Loading") {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.loading_img),
                contentDescription = stringResource(id = R.string.loading)
            )
            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text = loadingText)
        }
    }
}
