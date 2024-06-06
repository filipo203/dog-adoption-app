package com.example.dogadoption.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("HomeScreen")
    object ListScreen : Screen("DogListScreen")
    object PicsScreen : Screen("DogPicsScreen/{breed}")
    object PrevScreen : Screen("DogPreview/{imageId}")
    object UserScreen : Screen("UserScreen")
}