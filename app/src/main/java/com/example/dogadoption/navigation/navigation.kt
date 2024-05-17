package com.example.dogadoption.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dogadoption.ui.DogListScreen
import com.example.dogadoption.ui.DogPicturesScreen
import com.example.dogadoption.ui.DogPreviewScreen
import com.example.dogadoption.ui.HomeScreen
import com.example.dogadoption.ui.UserProfile
import com.example.dogadoption.viewmodels.DogViewModel

@Composable
fun Navigation(navController: NavHostController, dogViewModel: DogViewModel) {
    NavHost(navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController, dogViewModel)
        }
        composable(Screen.ListScreen.route) {
            DogListScreen(navController, dogViewModel)
        }
        composable(Screen.PicsScreen.route) {backStackEntry ->
            val breed = backStackEntry.arguments?.getString("breed") ?: ""
            DogPicturesScreen(navController, dogViewModel, breed)
        }
        composable(Screen.PrevScreen.route) {backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            DogPreviewScreen(navController, dogViewModel, index)
        }
        composable(Screen.UserScreen.route) {
            UserProfile(navController, dogViewModel)
        }
    }
}



