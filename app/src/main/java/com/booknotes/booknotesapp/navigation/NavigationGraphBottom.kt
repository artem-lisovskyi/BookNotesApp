package com.booknotes.booknotesapp.navigation

import android.app.Activity.RESULT_OK
import android.util.Log
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
import com.booknotes.booknotesapp.ui.screens.FavoritesScreen
import com.booknotes.booknotesapp.ui.screens.RecommendationsScreen
import com.booknotes.booknotesapp.ui.screens.home.HomeScreen
import com.booknotes.booknotesapp.ui.screens.information.InformationScreen
import com.booknotes.booknotesapp.ui.screens.presentation.OnboardingScreen
import com.booknotes.booknotesapp.ui.screens.presentation.ProfileScreen
import com.booknotes.booknotesapp.ui.screens.presentation.SignInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationGraphBottom(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: CoroutineScope,
    modifier: Modifier = Modifier
) {

    NavHost(navController = navController, startDestination = Destinations.Onboarding.route) {
        composable(Destinations.Home.route) {
            HomeScreen(navController = navController, modifier = modifier)
        }
        composable(Destinations.Favorites.route) {
            FavoritesScreen(modifier)
        }
        composable(Destinations.Recommendations.route) {
            RecommendationsScreen(modifier)
        }
        composable(Destinations.Profile.route) {
            ProfileScreen(modifier)
        }
        composable(Destinations.Onboarding.route) {
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
                if (state.isSignInSuccessful){
                    Log.i("SIGN IN", "SUCCESSFULL")
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
            Destinations.Information.route + "/{${Destinations.Information.argBookId}}",
            arguments = listOf(
                navArgument(Destinations.Information.argBookId)
                { type = NavType.StringType })
        ) {
            InformationScreen(
                modifier = modifier,
                bookId = it
                    .arguments
                    ?.getString(Destinations.Information.argBookId),
                navController = navController
            )
        }
    }
}