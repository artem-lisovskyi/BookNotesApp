package com.booknotes.booknotesapp.ui.screens.information

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.booknotes.booknotesapp.data.retrofit.Book
import com.booknotes.booknotesapp.data.room.BookEntity
import com.booknotes.booknotesapp.ui.MyTopAppBarWithBackButton
import com.booknotes.booknotesapp.ui.screens.ErrorScreen
import com.booknotes.booknotesapp.ui.screens.LoadingIndicator
import com.booknotesapp.booknotesapp.R
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(
    modifier: Modifier = Modifier,
    bookId: String?,
    context: Context,
    navController: NavHostController
) {

    val infoViewModel: InfoViewModel = viewModel(factory = InfoViewModel.Factory)
    infoViewModel.getInfoUiStateByBookId(bookId!!)

    val bookItem by produceState<BookEntity?>(initialValue = null, bookId) {
        value = infoViewModel.getBookFromRetrofit(bookId)
    }
    LaunchedEffect(bookId) {
        infoViewModel.getBookFromRetrofit(bookId)
    }

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

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
                Info(
                    infoUiState = infoViewModel.infoUiState,
                    scrollState = rememberScrollState(),
                    getImageVector = { infoViewModel.getImageVector(context, bookId, userId) },
                    onFavoriteClick = {
                        infoViewModel.toggleFavorite(context, bookId, userId, bookItem!!)
                    },
                    onLinkClick = { bookLink ->
                        infoViewModel.openWebsite(
                            context,
                            bookLink
                        )
                    },
                    retryAction = infoViewModel::getInfoUiStateByBookId
                )
            }
        }
    }
}

@Composable
fun Info(
    infoUiState: InfoUiState,
    retryAction: () -> Unit,
    scrollState: ScrollState,
    onFavoriteClick: () -> Boolean,
    getImageVector: () -> ImageVector,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (infoUiState) {
        is InfoUiState.Loading -> {
            LoadingIndicator(modifier)
        }

        is InfoUiState.Success -> {
            DetailedInfo(
                book = infoUiState.bookById,
                scrollState = scrollState,
                onFavoriteClick = onFavoriteClick,
                getImageVector = getImageVector,
                onLinkClick = onLinkClick,
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
    onFavoriteClick: () -> Boolean,
    getImageVector: () -> ImageVector,
    onLinkClick: (String) -> Unit,
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
                        text = stringResource(R.string.published_date) + "\n$it",
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
        MenuItem(
            onFavoriteClick = onFavoriteClick,
            getImageVector = getImageVector,
            onLinkClick = onLinkClick,
            bookItem = book
        )
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
                    .padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    onFavoriteClick: () -> Boolean,
    getImageVector: () -> ImageVector,
    bookItem: Book,
    onLinkClick: (String) -> Unit
) {
    var favoriteIcon by remember { mutableStateOf(getImageVector()) }
    var showDialog by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Icon(
            imageVector = favoriteIcon,
            tint = if (favoriteIcon == Icons.Default.Favorite) {
                Color(0xFFC51717)
            } else {
                Color(0xff3594d9)
            },
            contentDescription = stringResource(R.string.don_t_favorite),
            modifier = modifier
                .padding(top = 8.dp, start = 16.dp)
                .size(50.dp)
                .clickable {
                    favoriteIcon = if (onFavoriteClick()) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    }
                }
        )

        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(R.string.read),
            tint = Color(0xff3594d9),
            modifier = modifier
                .padding(top = 8.dp)
                .size(50.dp)
                .clickable { }
        )

        Icon(
            imageVector = Icons.Default.ArrowForward,
            tint = Color(0xff3594d9),
            contentDescription = stringResource(R.string.link),
            modifier = modifier
                .padding(top = 8.dp, end = 16.dp)
                .size(50.dp)
                .clickable {
                    showDialog = true
                }
        )
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color.Black,
            title = {
                Text(
                    text = stringResource(R.string.confirmation),
                    fontSize = 24.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.conformation_question),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            },
            shape = RectangleShape,
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff3594d9),
                        contentColor = Color.Black
                    ),
                    onClick = {
                        showDialog = false
                        onLinkClick(bookItem.previewLink!!)
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    ),
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}
