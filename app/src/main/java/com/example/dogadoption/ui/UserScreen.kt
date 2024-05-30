package com.example.dogadoption.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogadoption.room.user.UserEvent
import com.example.dogadoption.room.user.UserState
import com.example.dogadoption.viewmodels.DogViewModel
import com.example.dogadoption.viewmodels.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(
    navController: NavController,
    viewModel: UserViewModel
) {
    val user by viewModel.user.observeAsState()
    val uiState by viewModel.uiState.observeAsState(UserState())
    val favouriteDogs by viewModel.favoriteDogs.observeAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    if (user == null) {
                        Text(
                            text = "Your Profile",
                            maxLines = 1,
                            fontSize = 28.sp,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "${user!!.userName}'s profile",
                            maxLines = 1,
                            fontSize = 28.sp,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
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
                    if (user != null) {
                        IconButton(onClick = { viewModel.onEvent(UserEvent.ShowDialog) }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    ) {
        if (uiState.isAddingUser) {
            AddUserDialog(state = uiState, onEvent = viewModel::onEvent)
        }
        if (user == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    "Please enter your name",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Button(onClick = { viewModel.onEvent(UserEvent.ShowDialog) }) {
                    Text("Add name")
                }
            }
        } else {
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp)
            ) {
                Text(
                    "Favourite Dogs",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                if (favouriteDogs.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                    Text(
                        "Your favourite dogs will be displayed here!",
                        modifier = Modifier.fillMaxSize(),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )

                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        items(favouriteDogs!!) {dogImage ->
                            DogPictureItem(dogImages = dogImage) {
                                navController.navigate("DogPreview/${dogImage.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}
