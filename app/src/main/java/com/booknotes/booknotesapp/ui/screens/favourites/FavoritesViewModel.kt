package com.booknotes.booknotesapp.ui.screens.favourites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.booknotes.booknotesapp.BooksApplication
import com.booknotes.booknotesapp.data.retrofit.Book
import com.booknotes.booknotesapp.data.room.BooksRepositoryRoom
import com.booknotes.booknotesapp.navigation.DestinationsBottom
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface FavoriteUiState {
    data class Success(val favoritesBook: LiveData<List<Book>>) : FavoriteUiState
    object Error : FavoriteUiState
    object Loading : FavoriteUiState
}

class FavoritesViewModel(
    private val booksRepositoryRoom: BooksRepositoryRoom
) : ViewModel() {

    var favoriteUiState: FavoriteUiState by mutableStateOf(FavoriteUiState.Loading)
        private set

    init {
        getBooksFromDatabase()
    }

    fun getBooksFromDatabase() {
        viewModelScope.launch() {
            favoriteUiState = FavoriteUiState.Loading
            favoriteUiState = try {
                FavoriteUiState.Success(booksRepositoryRoom.allFavouriteBooks)
            } catch (e: IOException) {
                FavoriteUiState.Error
            } catch (e: HttpException) {
                FavoriteUiState.Error
            }
        }
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
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BooksApplication)
                val booksDatabase = application.getDatabase()
                FavoritesViewModel(booksRepositoryRoom = booksDatabase)
            }
        }
    }

}