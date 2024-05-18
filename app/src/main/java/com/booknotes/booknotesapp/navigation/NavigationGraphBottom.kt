package com.booknotes.booknotesapp.navigation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.booknotes.booknotesapp.signIn.GoogleAuthUiClient
import com.booknotes.booknotesapp.ui.screens.favourites.FavoritesScreen
import com.booknotes.booknotesapp.ui.screens.home.HomeScreen
import com.booknotes.booknotesapp.ui.screens.information.InformationScreen
import com.booknotes.booknotesapp.ui.screens.presentation.OnboardingScreen
import com.booknotes.booknotesapp.ui.screens.presentation.ProfileScreen
import com.booknotes.booknotesapp.ui.screens.presentation.SignInViewModel
import com.booknotes.booknotesapp.ui.screens.recommendations.RecommendationsScreen
import com.booknotesapp.booknotesapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationGraphBottom(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: CoroutineScope,
    appContext: Context,
    bottomNav: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = if (googleAuthUiClient.getSignedInUser() != null) {
            DestinationsBottom.Home.route
        } else {
            DestinationsBottom.Onboarding.route
        }
    ) {
        composable(DestinationsBottom.Home.route,
            ) {
            HomeScreen(navController = navController, modifier = modifier, bottomNav = bottomNav)
        }
        composable(DestinationsBottom.Favorites.route) {
            FavoritesScreen(navController = navController, modifier = modifier, bottomNav = bottomNav)
        }
        composable(DestinationsBottom.Recommendations.route) {
            RecommendationsScreen(navController = navController,modifier=modifier, bottomNav = bottomNav)
        }
        composable(DestinationsBottom.Profile.route) {
            ProfileScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                    }
                    Toast.makeText(appContext,
                        appContext.getString(R.string.successful_sign_out), Toast.LENGTH_SHORT).show()
                    navController.navigate(DestinationsBottom.Onboarding.route)
                },
                modifier = modifier,
                bottomNav = bottomNav,
                darkTheme = darkTheme,
                onThemeUpdated = onThemeUpdated
            )
            
        }
        composable(DestinationsBottom.Onboarding.route) {
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsState()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                })

            LaunchedEffect(key1 = state.isSignInSuccessful){
                if (state.isSignInSuccessful) {
                    Toast.makeText(appContext,
                        appContext.getString(R.string.successful_sign_in), Toast.LENGTH_SHORT).show()
                    navController.navigate(DestinationsBottom.Profile.route)
                    viewModel.resetState()
                }
            }

            OnboardingScreen(
                state = state,
                onSignInClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )

        }
        composable(
            DestinationsBottom.Information.route + "/{${DestinationsBottom.Information.argBookId}}",
            arguments = listOf(
                navArgument(DestinationsBottom.Information.argBookId)
                { type = NavType.StringType })
        ) {
            InformationScreen(
                modifier = modifier,
                bookId = it
                    .arguments
                    ?.getString(DestinationsBottom.Information.argBookId),
                context = appContext,
                navController = navController
            )
        }
    }
}