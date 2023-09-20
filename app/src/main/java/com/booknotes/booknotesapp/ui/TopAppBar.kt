package com.booknotes.booknotesapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.booknotesapp.booknotesapp.R

@Composable
fun MyTopAppBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.topbar),
            contentDescription = stringResource(R.string.logo),
            modifier = modifier.padding(16.dp)
        )
    }
}

@Composable
fun MyTopAppBarWithBackButton(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "",
            modifier = modifier
                .padding(start = 16.dp, top = 16.dp)
                .clickable { onBackClick() }
        )
        Image(
            painter = painterResource(id = R.drawable.topbar),
            contentDescription = stringResource(R.string.logo),
            modifier = modifier.padding(16.dp)
        )
    }
}