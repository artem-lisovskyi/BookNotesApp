package com.booknotes.booknotesapp.ui.screens.recommendations

import android.util.Log
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
import com.booknotes.booknotesapp.data.retrofit.RecommendationRetrofit
import com.booknotes.booknotesapp.data.room.BooksRepositoryRoom
import com.booknotes.booknotesapp.navigation.DestinationsBottom
import com.booknotes.booknotesapp.network.model.BookNames
import com.booknotes.booknotesapp.network.model.RecommendationResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException

sealed interface RecommendatinsUiState {
    data class Success(val bookSearch: List<Book>) : RecommendatinsUiState
    object Error : RecommendatinsUiState
    object Loading : RecommendatinsUiState
}

class RecommendationsViewModel(
    private val booksRepositoryRetrofit: BooksRepositoryRetrofit,
    private val recommendationRepositoryRetrofit: RecommendationRetrofit,
    private val booksRepositoryRoom: BooksRepositoryRoom
) : ViewModel() {

    var recommendatinsUiState: RecommendatinsUiState by mutableStateOf(RecommendatinsUiState.Loading)
        private set
    private var bookId = ""

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        getBooksForQueries()
    }

    fun checkDatabaseAndFetchBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            recommendatinsUiState = RecommendatinsUiState.Loading
            val isDatabaseEmpty = booksRepositoryRoom.countFavouriteBooks(userId) == 0
            if (isDatabaseEmpty) {
                getBooksForQueries()
            } else {
                getRecommendations()
            }

        }
    }

    private suspend fun getRecommendations() {
        recommendatinsUiState = RecommendatinsUiState.Loading
        var recommendations: List<String>
        val liveData = booksRepositoryRoom.allFavouriteBooksList(userId)
        val listOfTitles =
            liveData.map { it.title!! }
        Log.i("LIST OF FAVOURITES", listOfTitles.toString())
        val response =
            recommendationRepositoryRetrofit.getRecommendations(BookNames(listOfTitles))
        response.enqueue(object : retrofit2.Callback<RecommendationResponse> {
            override fun onResponse(
                call: Call<RecommendationResponse>,
                response: retrofit2.Response<RecommendationResponse>
            ) {
                if (response.isSuccessful) {
                    recommendations = response.body()?.recommendations ?: emptyList()
                    if (recommendations.isEmpty()) {
                        getBooksForQueries()
                    } else {
                        getBooksForQueries(recommendations)
                    }
                } else {
                    Log.e(
                        "Error with recommendations",
                        "Error: ${response.code()} ${response.errorBody()?.string()}"
                    )
                    recommendatinsUiState = RecommendatinsUiState.Error
                }
            }

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                Log.e("Error with recommendations", "Error: ${t.message}")
                recommendatinsUiState = RecommendatinsUiState.Error
            }
        })

    }


    fun getBooksForQueries(
        queries: List<String> = listOf(
            "Harry Potter",
            "Green Mile",
            "Divergent"
        ),
        maxResultsPerQuery: Int = 1,
        langRestrict: String = "en",
        orderBy: String = "relevance"
    ) {
        Log.i("LIST OF QUERIES BOOK(RECOMMENDATIONS)", queries.toString())
        viewModelScope.launch {

            val recommendationsList = mutableListOf<Book>()

            for (query in queries) {
                recommendatinsUiState = RecommendatinsUiState.Loading

                recommendatinsUiState = try {
                    val response =
                        booksRepositoryRetrofit.getBooks(
                            "intitle:$query",
                            maxResultsPerQuery,
                            langRestrict,
                            orderBy
                        )
                    val books = response.take(maxResultsPerQuery)
                    books.let { recommendationsList.addAll(it) }
                    RecommendatinsUiState.Success(recommendationsList)

                } catch (e: IOException) {
                    RecommendatinsUiState.Error
                } catch (e: HttpException) {
                    RecommendatinsUiState.Error
                }
            }
        }

    }

    fun setId(id: String) {
        bookId = id
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
                val recommendContainer = application.recommendContainer.recommendationRetrofit
                val booksDatabase = application.getDatabase()
                RecommendationsViewModel(
                    booksRepositoryRetrofit = booksRepository,
                    recommendationRepositoryRetrofit = recommendContainer,
                    booksRepositoryRoom = booksDatabase
                )
            }
        }
    }
}