package com.example.dogadoption.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.dogadoption.viewmodels.DogPicsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogPicturesScreen(navController: NavController, breed: String) {
    val viewModel = hiltViewModel<DogPicsViewModel>()
    val dogPictures by viewModel.dogPictures.collectAsState()

    LaunchedEffect(Unit) {
        Log.d(ContentValues.TAG, "Fetching dog pictures for breed: $breed")
        viewModel.fetchDogPictures(breed)
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
                        stringResource(com.example.dogadoption.R.string.app_name),
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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (dogPictures.isNotEmpty()) {
                Log.d(ContentValues.TAG, "Dog pictures fetched successfully: $dogPictures")
                LazyColumn {
                    items(dogPictures) { imageUrl ->
                        AsyncImage(model = imageUrl,
                            contentDescription = "Dog Picture",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { navController.navigate("DogPreview/$imageUrl") }
                        )

                    }
                }
            } else {
                Log.d(ContentValues.TAG, "No dog pictures available for breed: $breed")
                //CircularProgressIndicator()
                Text(
                    "Sorry, there's no pictures available for $breed.",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
            }
        }
    }
}

@Composable
fun DogPictureItem(imageUrl: String) {
    val painter = rememberAsyncImagePainter(model = imageUrl)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp)
    )
}