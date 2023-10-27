package com.booknotes.booknotesapp.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.booknotes.booknotesapp.BooksApplication
import com.booknotes.booknotesapp.data.retrofit.Book
import com.booknotes.booknotesapp.data.retrofit.BooksRepositoryRetrofit
import com.booknotes.booknotesapp.navigation.DestinationsBottom
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface HomeUiState {
    data class Success(val bookSearch: List<Book>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}


class HomeViewModel(
    private val booksRepositoryRetrofit: BooksRepositoryRetrofit
) : ViewModel() {

    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set


    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState


    init {
        getBooks()
    }


    fun getBooks(query: String = "book", maxResults: Int = 40) {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                HomeUiState.Success(booksRepositoryRetrofit.getBooks(query, maxResults))
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }


    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    fun navigateToScreen(
        screenRoute: String = DestinationsBottom.Information.route,
        navController: NavHostController,
        bookId: String
    ) {
        navController.navigate(screenRoute + "/{${bookId}}")
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BooksApplication)
                val booksRepository = application.container.booksRepositoryRetrofit
                HomeViewModel(booksRepositoryRetrofit = booksRepository)
            }
        }
    }

}