package com.booknotes.booknotesapp.ui.screens.home

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    //    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
//        private set
    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()
    private var bookId = ""

    private val _searchTextState = MutableStateFlow("")
    val searchTextState: StateFlow<String> = _searchTextState.asStateFlow()


    init {
        getBooks()
    }


    fun getBooks(
        query: String? = null,
        maxResults: Int = 15,
        langRestrict: String = "en",
        orderBy: String = "relevance"
    ) {
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Loading
            try {
                val books = booksRepositoryRetrofit.getBooks(
                        query = if (query.isNullOrBlank()) {
                            "subject:fiction"
                        } else {
                            "intitle:$query"
                        },
                        maxResults,
                        langRestrict,
                        orderBy
                )
                _homeUiState.value = HomeUiState.Success(books)
            } catch (e: IOException) {
                _homeUiState.value = HomeUiState.Error
            } catch (e: HttpException) {
                _homeUiState.value = HomeUiState.Error
            }
        }
    }

    fun setId(id: String) {
        bookId = id
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


