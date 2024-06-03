package com.example.dogadoption.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogadoption.ui.DogListScreen
import com.example.dogadoption.ui.DogPicturesScreen
import com.example.dogadoption.ui.DogPreviewScreen
import com.example.dogadoption.ui.HomeScreen
import com.example.dogadoption.ui.UserProfile
import com.example.dogadoption.viewmodels.DogPicsViewModel
import com.example.dogadoption.viewmodels.DogViewModel
import com.example.dogadoption.viewmodels.UserViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<DogViewModel>()
    val picsViewModel = hiltViewModel<DogPicsViewModel>()
    val userViewModel = hiltViewModel<UserViewModel>()

    NavHost(navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController, userViewModel)
        }
        composable(Screen.ListScreen.route) {
            DogListScreen(navController, viewModel)
        }
        composable(Screen.PicsScreen.route) {backStackEntry ->
            val breed = backStackEntry.arguments?.getString("breed") ?: ""
            DogPicturesScreen(navController, picsViewModel, breed)
        }
        composable(
            Screen.PrevScreen.route,
            arguments = listOf(navArgument("imageId") { type = NavType.IntType }))
        { backStackEntry ->
            val imageId = backStackEntry.arguments?.getInt("imageId") ?: 0
            DogPreviewScreen(navController, picsViewModel, imageId)
        }
        composable(Screen.UserScreen.route) {
            UserProfile(navController, userViewModel)
        }
    }
}
