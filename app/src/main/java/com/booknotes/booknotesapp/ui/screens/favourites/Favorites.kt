package com.booknotes.booknotesapp.ui.screens.favourites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.booknotes.booknotesapp.data.room.BookEntity
import com.booknotes.booknotesapp.ui.MyTopAppBar
import com.booknotes.booknotesapp.ui.screens.ErrorScreen
import com.booknotes.booknotesapp.ui.screens.LoadingIndicator
import com.booknotesapp.booknotesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomNav: @Composable () -> Unit
) {
    val favoritesViewModel: FavoritesViewModel =
        viewModel(factory = FavoritesViewModel.Factory)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MyTopAppBar() },
        bottomBar = bottomNav// { MyBottomNavigation(navController = navController)}
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            Favorite(favoriteUiState = favoritesViewModel.favoriteUiState,
                retryAction = { favoritesViewModel.getBooksFromDatabase() },
                onItemClick = {
                    favoritesViewModel.navigateToScreen(
                        navController = navController,
                        bookId = it
                    )
                })
        }
    }
}


@Composable
fun Favorite(
    favoriteUiState: FavoriteUiState,
    retryAction: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (favoriteUiState) {
        is FavoriteUiState.Loading -> {
            LoadingIndicator(modifier)
        }

        is FavoriteUiState.Success -> {
            ListBooks(
                books = favoriteUiState.favoritesBook,
                modifier = modifier,
                onItemClick = onItemClick
            )
        }

        is FavoriteUiState.Error -> {
            ErrorScreen(
                retryAction = retryAction,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ListBooks(
    books: LiveData<List<BookEntity>>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    val booksList = books.observeAsState().value
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .padding(top = 16.dp)
    ) {
        if (booksList != null) {
            itemsIndexed(booksList) { _, book ->
                BookItem(book, modifier, onItemClick)
            }
        }
    }
}

@Composable
fun BookItem(
    book: BookEntity,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable {
                book.id.let {
                    onItemClick(it)
                }
            }
    ) {

        AsyncImage(
            modifier = modifier
                .size(100.dp, 150.dp),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(
                    book.imageLink?.replace(
                        "http:",
                        "https:"
                    )
                )
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_book_default),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentDescription = stringResource(R.string.poster),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 8.dp)

        ) {
            book.title?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = modifier.fillMaxWidth()
                )
            }
            book.authors?.let { listAuthors ->
                listAuthors.map {
                    Text(
                        text = it,
                        fontSize = 12.sp
                    )
                }
            }
            book.publishedDate?.let {
                Text(
                    text = it,
                    modifier = modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
    Divider(
        modifier = Modifier.padding(8.dp),
        color = Color.LightGray
    )
}
