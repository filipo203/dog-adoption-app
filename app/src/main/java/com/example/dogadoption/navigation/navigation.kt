package com.example.dogadoption.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
fun Navigation(
    navController: NavHostController,
    dogViewModel: DogViewModel,
    picsViewModel: DogPicsViewModel,
    userViewModel: UserViewModel
) {
    NavHost(navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController, userViewModel)
        }
        composable(Screen.ListScreen.route) {
            DogListScreen(navController, dogViewModel)
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
