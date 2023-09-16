package com.booknotes.booknotesapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun InformationScreen(bookId: String?) {
    Column {
    MyTopAppBar()


        Text(text= bookId?: "")
    }
}