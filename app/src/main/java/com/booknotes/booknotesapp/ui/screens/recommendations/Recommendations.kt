package com.booknotes.booknotesapp.ui.screens.recommendations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.booknotes.booknotesapp.ui.MyTopAppBar
import com.booknotes.booknotesapp.ui.screens.ErrorScreen
import com.booknotes.booknotesapp.ui.screens.LoadingIndicator
import com.booknotes.booknotesapp.ui.screens.home.ListBooks
import com.booknotesapp.booknotesapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    bottomNav: @Composable () -> Unit
) {
    val recommendationsViewModel: RecommendationsViewModel =
        viewModel(factory = RecommendationsViewModel.Factory)
    var isLoading by remember { mutableStateOf(false) }

    recommendationsViewModel.recommendatinsUiState

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MyTopAppBar() },
        bottomBar = bottomNav//{ MyBottomNavigation(navController = navController) }

    ) { it ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            isLoading = true
                            recommendationsViewModel.checkDatabaseAndFetchBooks()
                        },
                        enabled = !isLoading
                    ) {
                        Text(stringResource(R.string.update_recommendations))
                    }
                }
                Recommendations(recommendationsUiState = recommendationsViewModel.recommendatinsUiState,
                    retryAction = { recommendationsViewModel.getBooksForQueries() },
                    onItemClick = {
                        recommendationsViewModel.navigateToScreen(
                            navController = navController,
                            bookId = it
                        )
                        recommendationsViewModel.setId(it)
                    })
            }

        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { recommendationsViewModel.recommendatinsUiState }
            .collect { value ->
                when (value) {
                    is RecommendatinsUiState.Success -> {
                        isLoading = false
                    }
                    is RecommendatinsUiState.Error -> {
                        isLoading = false
                    }
                    is RecommendatinsUiState.Loading -> {}
                }
            }
    }
}

@Composable
fun Recommendations(
    recommendationsUiState: RecommendatinsUiState,
    retryAction: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (recommendationsUiState) {
        is RecommendatinsUiState.Loading -> {
            LoadingIndicator(modifier, "Making recommendations. Please wait!")
        }

        is RecommendatinsUiState.Success -> {
            ListBooks(
                books = recommendationsUiState.bookSearch,
                modifier = modifier,
                onItemClick = onItemClick
            )
        }

        is RecommendatinsUiState.Error -> {
            ErrorScreen(
                retryAction = retryAction,
                modifier = modifier
            )
        }
    }
}