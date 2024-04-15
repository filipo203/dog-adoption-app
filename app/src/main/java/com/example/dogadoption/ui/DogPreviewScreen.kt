package com.example.dogadoption.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.dogadoption.R
import com.example.dogadoption.navigation.Screen
import com.example.dogadoption.viewmodels.DogPicsViewModel
import java.net.URLDecoder


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogPreviewScreen(navController: NavController, viewModel: DogPicsViewModel, index: Int) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val breed = navController.previousBackStackEntry?.arguments?.getString("breed") ?: ""
    val imageUrl = navController.previousBackStackEntry?.arguments?.getString("imageUrl") ?: ""

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
                        stringResource(R.string.app_name),
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(66.dp))
            Text(
                text = "Breed: $breed",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            // Observe dogPicturesLiveData from the ViewModel
            val dogPictures by viewModel.dogPicturesLiveData.observeAsState(emptyList())
            //val error by viewModel.errorLiveData.observeAsState("")

            if (dogPictures.isNotEmpty()) {
                if (index in dogPictures.indices) {
                    DogPicture(imageUrl = dogPictures[index])
                } else {
                    Text(text = "Invalid index: $index")
                }
                //DogPicture(imageUrl = dogPictures.getOrNull(selectedIndex) ?: "")
            }
            Button(onClick = { /* TODO Handle adopt button click */ }) {
                Text("Adopt")
            }
        }
    }
}

@Composable
fun DogPicture(imageUrl: String) {
    val painter = rememberAsyncImagePainter(imageUrl)
    Box(modifier = Modifier) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .padding(2.dp)
        )
        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

/*
    val breed = remember {
        navController.previousBackStackEntry?.arguments?.getString("breed")
    } ?: ""
    val imageUrl = remember {
        navController.previousBackStackEntry?.arguments?.getString("imageUrl")
    } ?: ""

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
                        stringResource(R.string.app_name),
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(66.dp))
            Text(
                text = "Breed: $breed",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            rememberAsyncImagePainter(model = imageUrl)
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        scale(Scale.FILL)
                    }).build()
            )
            if (imageUrl.isNotEmpty()) {
                rememberAsyncImagePainter(model = imageUrl)
                Image(painter = painter, contentDescription = null, Modifier.size(250.dp))
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    Modifier.size(250.dp)
                )
                DogPicture(imageUrl = imageUrl)
            } else {
                Text(text = "Image URL is empty or invalid")
            }
            Button(onClick = { /* TODO Handle adopt button click */ }) {
                Text("Adopt")
            }
        }
    }
}

@Composable
fun DogPicture(imageUrl: String) {
    val painter = rememberAsyncImagePainter(model = imageUrl)
    Box(modifier = Modifier){
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(250.dp)
            .padding(2.dp)
    )}
}

 */