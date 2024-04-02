package com.example.dogadoption.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dogadoption.ui.DogListScreen
import com.example.dogadoption.ui.DogPicturesScreen
import com.example.dogadoption.ui.DogPreviewScreen
import com.example.dogadoption.ui.HomeScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = "HomeScreen") {
        composable("HomeScreen") {
            HomeScreen(navController)
        }
        composable(route = "DogListScreen") {
            DogListScreen(navController)
        }
        composable("DogPicsScreen/{breed}") {backStackEntry ->
            val breed = backStackEntry.arguments?.getString("breed") ?: ""
            DogPicturesScreen(navController, breed = breed)
        }
        composable("DogPreview/{imageUrl}") {backStackEntry ->
            val breed = backStackEntry.arguments?.getString("breed") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""

            DogPreviewScreen(navController, breed, imageUrl = imageUrl)
        }

    }
}



