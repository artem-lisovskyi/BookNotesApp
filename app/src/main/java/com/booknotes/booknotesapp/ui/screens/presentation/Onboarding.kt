package com.booknotes.booknotesapp.ui.screens.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.booknotes.booknotesapp.signIn.SignInState
import com.booknotesapp.booknotesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInErrorMessage) {
        state.signInErrorMessage?.let { error ->
            Toast.makeText(
                context, error, Toast.LENGTH_SHORT
            ).show()
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.topbar),
                    contentDescription = stringResource(R.string.logo),
                    modifier = modifier
                        .size(300.dp)
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = stringResource(R.string.sign_in_phrase),
                    color = Color.Gray,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(50.dp))
                Button(
                    shape = AbsoluteRoundedCornerShape(10.dp),
                    onClick = onSignInClick,
                    modifier = Modifier.size(200.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3295DD)
                    )
                ) {
                    Text(text = stringResource(R.string.sign_in))
                }
            }

        }
    }
}