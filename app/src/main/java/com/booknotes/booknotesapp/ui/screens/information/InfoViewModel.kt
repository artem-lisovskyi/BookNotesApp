package com.booknotes.booknotesapp.ui.screens.information

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.booknotes.booknotesapp.BooksApplication
import com.booknotes.booknotesapp.data.SaveShared
import com.booknotes.booknotesapp.data.retrofit.Book
import com.booknotes.booknotesapp.data.retrofit.BooksRepositoryRetrofit
import com.booknotes.booknotesapp.data.room.BookEntity
import com.booknotes.booknotesapp.data.room.BooksRepositoryRoom
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
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

    fun toggleFavorite(
        context: Context,
        bookId: String,
        userId: String,
        bookItem: BookEntity
    ): Boolean {
        val isFavorite = getFavoriteState(context, bookId, userId)
        if (isFavorite) {
            // Remove from favorites
            deleteBookFromDatabase(bookItem) {
                SaveShared.setFavorite(context, bookId, false, userId)
                Log.i("DATABASE", "Success delete record")
            }
        } else {
            // Add to favorites
            addBookToDatabase(bookItem) {
                SaveShared.setFavorite(context, bookId, true, userId)
                Log.i("DATABASE", "Success insert new record")
            }
        }
        return !isFavorite
    }

    private fun getFavoriteState(context: Context, bookId: String, userId: String): Boolean {
        return SaveShared.getFavorite(context, bookId, userId)
    }

    fun getImageVector(context: Context, bookId: String, userId: String): ImageVector {
        return if (getFavoriteState(context, bookId, userId)) {
            Icons.Default.Favorite
        } else {
            Icons.Default.FavoriteBorder
        }
    }

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

    private fun addBookToDatabase(bookItem: BookEntity, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepositoryRoom.insertFavouriteBook(bookItem = bookItem) {
                onSuccess()
            }
        }

    }

    private fun deleteBookFromDatabase(bookItem: BookEntity, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepositoryRoom.deleteFavouriteBook(bookItem = bookItem) {
                onSuccess()
            }
        }
    }

    fun backNavigation(navController: NavHostController) {
        navController.navigateUp()
    }

    suspend fun getBookFromRetrofit(bookId: String): BookEntity {
        val book = booksRepositoryRetrofit.getBookById(bookId)
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        return BookEntity(
            id = book.id,
            userId = currentUserUid,
            title = book.title,
            authors = book.authors,
            publishedDate = book.publishedDate,
            description = book.description,
            pageCount = book.pageCount,
            categories = book.categories,
            imageLink = book.imageLink,
            previewLink = book.previewLink
        )
    }

    fun openWebsite(context: Context, bookLink: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(bookLink)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
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