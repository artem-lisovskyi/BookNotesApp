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
import com.booknotes.booknotesapp.data.Book
import com.booknotes.booknotesapp.data.BooksRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface InfoUiState {
    data class Success(val bookById: Book) : InfoUiState
    object Error : InfoUiState
    object Loading : InfoUiState
}

class InfoViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    var infoUiState: InfoUiState by mutableStateOf(InfoUiState.Loading)
        private set

    fun getBookById(bookId: String = "") {
        viewModelScope.launch {
            infoUiState = try {
                InfoUiState.Success(booksRepository.getBookById(bookId))
            } catch (e: IOException) {
                InfoUiState.Error
            } catch (e: HttpException) {
                InfoUiState.Error
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
                val booksRepository = application.container.booksRepository
                InfoViewModel(booksRepository = booksRepository)
            }
        }
    }
}