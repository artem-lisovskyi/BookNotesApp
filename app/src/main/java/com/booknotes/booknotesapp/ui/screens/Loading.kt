package com.booknotes.booknotesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier, loadingText: String = "Loading") {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),

        ) {
            CircularProgressIndicator(
                modifier = modifier
                    .size(50.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = loadingText,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingIndicatorPreview() {
    LoadingIndicator()
}