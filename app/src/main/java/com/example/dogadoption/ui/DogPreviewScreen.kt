package com.example.dogadoption.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.dogadoption.room.dogs.DogImages
import com.example.dogadoption.viewmodels.DogPicsViewModel
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogPreviewScreen(
    navController: NavController,
    viewModel: DogPicsViewModel,
    imageId: Int)
{
    val dogImagesData by viewModel.dogImageData.collectAsState()
    val dogImageData = dogImagesData.find { it.id == imageId }
    val favouriteImages by viewModel.favoriteDogs.collectAsState()
    val favouriteDogImage = favouriteImages.find { it.id == imageId }

    // for data handling all dogs
    val allDogImages = (dogImagesData + ((favouriteImages).distinctBy { it.id }))
    val allDogImage = allDogImages.find { it.id == imageId }
    val breed = allDogImage?.breedName ?: ""

    LaunchedEffect(breed) {
        Log.d("DOGPREVIEWSCREEN", "Fetching dog pictures")
        viewModel.saveDogPictures(breed)
        viewModel.getDogImages(breed)
        viewModel.favoriteDogs.value
    }
    if (allDogImage == null) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Error",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 150.dp)
            )
            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 50.dp)
            ) {
                Text("Go back")
            }
            Log.d(
                "DOGPREVIEWSCREEN",
                "NULL: Dog Image: $allDogImage, ${allDogImage?.id} / Dog Data Size: ${dogImagesData.size}")
        }
        return
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
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.toggleFavourite(allDogImage)
                        },
                        modifier = Modifier.pulseClick()
                    ) {
                        Icon(
                            imageVector = if (allDogImage.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = if (allDogImage.isFavourite) Color.Red else Color.Black
                        )
                    }
                }
            )
        }
    ) {
        if (favouriteDogImage == null) {
            DogPicture(dogImages = dogImageData!!)
        } else {
            DogPicture(dogImages = favouriteDogImage)
        }
        Log.d(
            "DOGPREVIEWSCREEN",
            "DogPicture(), Dog Image: $allDogImage / Dog Data Size for dog $breed: ${dogImagesData.size}")
    }
}

@Composable
fun DogPicture(dogImages: DogImages) {
    val painter = rememberAsyncImagePainter(dogImages.imageUrls)
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
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Text("Adopt")
            }
        }
    }
}

fun Modifier.pulseClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.7f else 1f,
        label = ""
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

enum class ButtonState { Pressed, Idle }