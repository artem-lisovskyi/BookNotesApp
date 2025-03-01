package com.booknotes.booknotesapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.booknotes.booknotesapp.data.retrofit.Book
import com.booknotes.booknotesapp.ui.MyTopAppBar
import com.booknotes.booknotesapp.ui.screens.ErrorScreen
import com.booknotes.booknotesapp.ui.screens.LoadingIndicator
import com.booknotesapp.booknotesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomNav: @Composable () -> Unit
) {
    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)
    val searchTextState by homeViewModel.searchTextState.collectAsState()
    val homeUiState by homeViewModel.homeUiState.collectAsState()

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
            Column {
                TextField(
                    search = searchTextState,
                    onTextChange = homeViewModel::updateSearchTextState,
                    onSearchClicked = homeViewModel::getBooks
                )
                Home(homeUiState = homeUiState,
                    retryAction = homeViewModel::getBooks,
                    onItemClick = { bookId ->
                        homeViewModel.navigateToScreen(
                            navController = navController,
                            bookId = bookId
                        )
                        homeViewModel.setId(bookId)
                    })
            }
        }
    }
}


@Composable
fun Home(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (homeUiState) {
        is HomeUiState.Loading -> LoadingIndicator(modifier)
        is HomeUiState.Success -> ListBooks(
            books = homeUiState.bookSearch,
            modifier = modifier,
            onItemClick = onItemClick
        )

        is HomeUiState.Error -> ErrorScreen(retryAction = retryAction, modifier = modifier)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    search: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    OutlinedTextField(
        value = search,
        placeholder = { Text(text = stringResource(R.string.start_searching_for_new_books)) },
        onValueChange = onTextChange,
        textStyle = TextStyle(color = Color.Gray),
        trailingIcon = {
            IconButton(
                onClick = { onSearchClicked(search) },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                )
            }
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked(search) }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 24.dp)
    )
}

@Composable
fun ListBooks(
    books: List<Book>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),

        ) {
        items(books) { book ->
            BookItem(book = book, onItemClick = onItemClick)
        }

    }
}

@Composable
fun BookItem(
    book: Book,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable {
                onItemClick(book.id)
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