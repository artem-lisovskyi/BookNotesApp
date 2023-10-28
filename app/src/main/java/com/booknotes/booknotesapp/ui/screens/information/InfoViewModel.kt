package com.booknotes.booknotesapp.ui.screens.information

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.booknotes.booknotesapp.BooksApplication
import com.booknotes.booknotesapp.data.retrofit.Book
import com.booknotes.booknotesapp.data.retrofit.BooksRepositoryRetrofit
import com.booknotes.booknotesapp.data.room.BooksRepositoryRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface InfoUiState {
    data class Success(val bookById: Book) : InfoUiState
    object Error : InfoUiState
    object Loading : InfoUiState
}

class InfoViewModel(
    private val booksRepositoryRetrofit: BooksRepositoryRetrofit,
    private val booksRepositoryRoom: BooksRepositoryRoom
) : ViewModel() {

    var infoUiState: InfoUiState by mutableStateOf(InfoUiState.Loading)
        private set

    fun getInfoUiStateByBookId(bookId: String = "") {
        viewModelScope.launch {
            infoUiState = try {
                InfoUiState.Success(booksRepositoryRetrofit.getBookById(bookId))
            } catch (e: IOException) {
                InfoUiState.Error
            } catch (e: HttpException) {
                InfoUiState.Error
            }
        }
    }

    suspend fun getBookById(bookId: String): Book {
        return viewModelScope.async {
            booksRepositoryRetrofit.getBookById(bookId)
        }.await()
    }

    fun addBookToDatabase(bookItem: Book, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepositoryRoom.insertFavouriteBook(bookItem = bookItem) {
                onSuccess()
            }
        }

    }

    fun deleteBookFromDatabase(bookItem: Book, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepositoryRoom.deleteFavouriteBook(bookItem = bookItem) {
                onSuccess()
            }
        }
    }

    fun backNavigation(navController: NavHostController) {
        navController.navigateUp()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BooksApplication)
                val booksRepository = application.container.booksRepositoryRetrofit
                val booksDatabase = application.getDatabase()
                InfoViewModel(
                    booksRepositoryRetrofit = booksRepository,
                    booksRepositoryRoom = booksDatabase
                )
            }
        }
    }
}