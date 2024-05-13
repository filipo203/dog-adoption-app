package com.example.dogadoption.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.dogadoption.viewmodels.DogViewModel
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogPreviewScreen(navController: NavController, viewModel: DogViewModel, index: Int) {

    val breed = navController.previousBackStackEntry?.arguments?.getString("breed") ?: ""

    LaunchedEffect(Unit) {
        Log.d(ContentValues.TAG, "Fetching dog pictures for breed: $breed")
        viewModel.getDogImages(breed)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        breed.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                            else it.toString() },
                        maxLines = 1,
                        fontSize = 28.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
            val dogPictures by viewModel.dogImages.observeAsState(emptyList())
            if (dogPictures.isNotEmpty()) {
                if (index in dogPictures.indices) {
                    DogPicture(dogPictures[index])
                } else {
                    Text("Invalid index: $index")
                }
            }

    }
}

@Composable
fun DogPicture(imageUrl: String) {
    val painter = rememberAsyncImagePainter(imageUrl)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 66.dp)
    ) {
        if (painter.state is AsyncImagePainter.State.Error) {
            Text(
                text = "No image available to preview",
                fontSize = 24.sp,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 150.dp)
            )
        } else {
            Image(
                painter,
                contentDescription = null,
                modifier = Modifier
                    .size(400.dp)
                    .padding(2.dp)
            )
            Button(
                onClick = { /* TODO Handle adopt button click */ },
                modifier = Modifier.align(CenterHorizontally)) {
                Text("Adopt")
            }
        }
    }
}
