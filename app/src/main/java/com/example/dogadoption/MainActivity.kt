package com.example.dogadoption

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.dogadoption.navigation.Navigation
import com.example.dogadoption.ui.theme.DogAdoptionTheme
import com.example.dogadoption.viewmodels.DogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogAdoptionTheme {
                val navController = rememberNavController()
                val viewModel = hiltViewModel<DogViewModel>()
                Navigation(navController, viewModel)
            }
        }
    }
}
