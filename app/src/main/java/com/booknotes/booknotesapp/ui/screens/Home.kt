package com.booknotes.booknotesapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.booknotes.booknotesapp.api.BooksApi
import com.booknotes.booknotesapp.api.RetrofitClient
import com.booknotes.booknotesapp.model.Book
import com.booknotes.booknotesapp.model.Books
import com.booknotes.booknotesapp.navigation.DestinationsBottom
import com.booknotesapp.booknotesapp.R
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@Composable
fun HomeScreen(navController: NavHostController) {
    Column {
        MyTopAppBar()
        HomeDesign(navController)
    }
}


@Composable
fun MyTopAppBar() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.topbar),
            contentDescription = stringResource(R.string.logo),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDesign(navController: NavHostController) {
    var search by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val quotesApi = RetrofitClient.getInstance().create(BooksApi::class.java)
    var userResponce: Response<Books>
    var result by remember {
        mutableStateOf(listOf<Book>())
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        OutlinedTextField(
            value = search,
            placeholder = { Text(text = stringResource(R.string.start_searching_for_new_books)) },
            onValueChange = {
                search = it
            },
            textStyle = TextStyle(color = Color.DarkGray),
            trailingIcon = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                userResponce = quotesApi.getBooks(search)
                                result = userResponce.body()?.items ?: listOf()
                                Log.i("RESPONSE", result.toString())
                            } catch (e: IOException) {
                                Log.e("ERROR", "IO network error", e)
                            } catch (e: HttpException) {
                                Log.e("ERROR", "HTTP network error", e)
                            }
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 32.dp)
        )
        BookItemsList(books = result, navController)
    }
}

@Composable
fun BookItemsList(books: List<Book>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 20.dp, bottom = 70.dp)
    ) {
        items(
            items = books,
            itemContent = { bookItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .clickable {
                            navController
                                .navigate(
                                    DestinationsBottom.Information.route +
                                            "/{${bookItem.id}}"
                                )
                        }
                ) {

                    AsyncImage(
                        modifier = Modifier
                            .size(100.dp, 150.dp),
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(
                                bookItem.volumeInfo?.imageLinks?.smallThumbnail?.replace(
                                    "http:",
                                    "https:"
                                )
                            )
                            .crossfade(true)
                            .build(),
                        error = painterResource(id = R.drawable.ic_book_default),
                        placeholder = painterResource(id = R.drawable.ic_book_default),
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
                        Text(
                            text = bookItem.volumeInfo?.title
                                ?: stringResource(R.string.title_not_found),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        bookItem.volumeInfo?.authors?.map {
                            Text(
                                text = it,
                                fontSize = 12.sp
                            )
                        }
                            ?: Text(
                                stringResource(R.string.authors_not_found),
                                fontSize = 12.sp
                            )

                        Text(
                            text = bookItem.volumeInfo?.publishedDate
                                ?: stringResource(R.string.published_date_not_found),
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                        )

                    }
                }
                Divider(
                    modifier = Modifier.padding(8.dp),
                    color = Color.LightGray
                )
            }
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
}