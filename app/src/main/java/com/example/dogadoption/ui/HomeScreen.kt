package com.example.dogadoption.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogadoption.R
import com.example.dogadoption.viewmodels.DogViewModel
import com.example.dogadoption.viewmodels.UserViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: UserViewModel) {
    val user by viewModel.user.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            fontSize = 36.sp,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        if (user != null){
            Text(
                "Hello ${user!!.userName}!",
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text( // TODO: Replace text with logo
            "logo here",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 30.dp, vertical = 45.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            navController.navigate("DogListScreen")
        }
        ) {
            Text(
                "See Dogs",
                color = Color.White
            )
        }
    }
}