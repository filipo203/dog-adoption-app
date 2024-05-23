package com.example.dogadoption.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.dogadoption.room.dogs.DogImages
import com.example.dogadoption.viewmodels.DogViewModel
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogPicturesScreen(
    navController: NavController,
    viewModel: DogViewModel,
    breed: String
) {
    val dogImageData by viewModel.dogImageData.observeAsState()
    val loading by viewModel.loading.observeAsState()

    LaunchedEffect(breed) {
        Log.d(ContentValues.TAG, "Fetching dog pictures for breed: $breed")
        viewModel.getDogImages(breed)
        viewModel.saveDogPictures(breed)
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
                            else it.toString()
                        },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (loading == true) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = "Downloading Images...",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            } else if (dogImageData != null && dogImageData!!.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(dogImageData!!) { index, dogData ->
                        val dogImage = dogImageData?.get(index)
                        if (dogImage != null) {
                            DogPictureItem(dogImage) {
                                navController.navigate(
                                    "DogPreview/{breed}/{selectedImageUrl}/${index}"
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    "Offline: \n Cannot access dog images",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 35.sp
                )
            }
        }
    }
}

@Composable
fun DogPictureItem(dogImages: DogImages, onClick: (String) -> Unit) {
    val painter = rememberAsyncImagePainter(model = dogImages.imageUrls)
    Box(Modifier.clickable {
        Log.e(
            ContentValues.TAG,
            "DogPictureItem: Clicked image URL: ${dogImages.imageUrls}, DogImages = $dogImages"
        )
        onClick.invoke(dogImages.imageUrls)
    }) {
        Image(
            painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .size(250.dp)
                .padding(2.dp)
        )
        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (painter.state is AsyncImagePainter.State.Error) {
            Text(
                text = "Error loading image",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}