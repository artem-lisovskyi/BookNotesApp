package com.booknotes.booknotesapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.booknotes.booknotesapp.navigation.MyBottomNavigation
import com.booknotes.booknotesapp.navigation.NavigationGraphBottom
import com.booknotes.booknotesapp.signIn.GoogleAuthUiClient
import com.booknotes.booknotesapp.ui.theme.BookNotesTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookNotesTheme {
                MyNavigation(googleAuthUiClient)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyNavigation(googleAuthUiClient: GoogleAuthUiClient) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { MyBottomNavigation(navController = navController) }
    ) {
        NavigationGraphBottom(
            navController = navController, googleAuthUiClient,
            CoroutineScope(Dispatchers.Default), Modifier.padding(it)
        )
    }
}