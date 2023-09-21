package com.booknotes.booknotesapp.ui.screens.information

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.booknotes.booknotesapp.data.Book
import com.booknotes.booknotesapp.ui.MyTopAppBarWithBackButton
import com.booknotes.booknotesapp.ui.screens.ErrorScreen
import com.booknotes.booknotesapp.ui.screens.LoadingScreen
import com.booknotesapp.booknotesapp.R
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(
    modifier: Modifier = Modifier,
    bookId: String?,
    navController: NavHostController
) {
    val infoViewModel: InfoViewModel = viewModel(factory = InfoViewModel.Factory)
    infoViewModel.getBookById(bookId!!)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MyTopAppBarWithBackButton(
                modifier = Modifier,
                onBackClick = { infoViewModel.backNavigation(navController = navController) }
            )
        }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Info(infoUiState = infoViewModel.infoUiState,
                    scrollState = rememberScrollState(),
                    retryAction = { infoViewModel.getBookById(bookId) })
            }
        }
    }
}

@Composable
fun Info(
    infoUiState: InfoUiState,
    retryAction: () -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    when (infoUiState) {
        is InfoUiState.Loading -> {
            LoadingScreen(modifier)
        }

        is InfoUiState.Success -> {
            DetailedInfo(
                book = infoUiState.bookById,
                scrollState = scrollState,
                modifier = modifier
            )
        }

        is InfoUiState.Error -> {
            ErrorScreen(
                retryAction = retryAction, modifier = modifier
            )
        }
    }
}

@Composable
fun DetailedInfo(
    book: Book,
    scrollState: ScrollState,
    modifier: Modifier
) {
    Column(
        modifier
            .padding(16.dp)
            .verticalScroll(scrollState, true)
    ) {
        Row {
            AsyncImage(
                modifier = modifier.size(150.dp, 200.dp),
                model = ImageRequest.Builder(context = LocalContext.current).data(
                    book.imageLink?.replace(
                        "http:", "https:"
                    )
                ).crossfade(true).build(),
                error = painterResource(id = R.drawable.ic_book_default),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentDescription = stringResource(R.string.poster),
                contentScale = ContentScale.Crop
            )
            Column(modifier.padding(start = 8.dp)) {
                book.title?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Gray,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                book.authors?.let { listAuthors ->
                    listAuthors.map {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                book.publishedDate?.let {
                    Text(
                        text = "Published date:\n$it",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }

                book.categories?.let { categoriesList ->
                    if (categoriesList.isNotEmpty()) {
                        Text(
                            text = categoriesList[0],
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        MenuItem()
        book.description?.let {
            Text(
                text = Pattern
                    .compile("<[^>]*>")
                    .matcher(
                        it
                            .replace("\n", "")
                    )
                    .replaceAll(""),
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )
        }
    }
}

@Composable
fun MenuItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Column(modifier = modifier.clickable { }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.don_t_favorite),
                modifier = modifier
                    .size(50.dp)
            )
            Text(
                text = "Add to favorite",
                fontSize = 8.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }

//        Column(modifier = modifier.clickable { }) {
//            Icon(
//                imageVector = Icons.Default.Check,
//                contentDescription = stringResource(R.string.don_t_favorite),
//                modifier = modifier
//                    .size(50.dp)
//            )
//            Text(
//                text = "Mark as read",
//                fontSize = 8.sp,
//                color = Color.White,
//                modifier = Modifier
//                    .padding(top = 8.dp)
//            )
//        }
//
//        Column(modifier = modifier.clickable { }) {
//
//
//            Icon(
//                imageVector = Icons.Default.ArrowForward,
//                contentDescription = stringResource(R.string.don_t_favorite),
//                modifier = modifier
//                    .size(50.dp)
//            )
//            Text(
//                text = "Go to website",
//                fontSize = 8.sp,
//                color = Color.White,
//                modifier = Modifier
//                    .padding(top = 8.dp)
//            )
//        }
    }
}