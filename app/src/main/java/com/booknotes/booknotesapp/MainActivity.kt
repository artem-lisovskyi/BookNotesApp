package com.booknotes.booknotesapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            var darkTheme by remember { mutableStateOf(false) }
            BookNotesTheme(darkTheme = darkTheme) {
                MyNavigation(
                    googleAuthUiClient = googleAuthUiClient,
                    appContext = applicationContext,
                    darkTheme = darkTheme
                ) { darkTheme = !darkTheme }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyNavigation(
    googleAuthUiClient: GoogleAuthUiClient,
    appContext: Context,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit
) {
    val navController = rememberNavController()
    val bottomNav: @Composable () -> Unit =
        remember { @Composable { MyBottomNavigation(navController = navController) } }

    Scaffold(
        // bottomBar = { MyBottomNavigation(navController = navController) }
    ) {
        NavigationGraphBottom(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            lifecycleScope = CoroutineScope(Dispatchers.Default),
            appContext = appContext,
            bottomNav = bottomNav,
            modifier = Modifier.padding(it),
            darkTheme = darkTheme,
            onThemeUpdated = onThemeUpdated
        )
    }
}




