package com.example.dogadoption.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dogadoption.room.user.UserEvent
import com.example.dogadoption.room.user.UserState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserDialog(
    state: UserState,
    onEvent: (UserEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState, modifier = Modifier.imePadding()) }
    ) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onEvent(UserEvent.HideDialog) },
            title = { Text(text = "What is your name?") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = state.userName,
                        onValueChange = { onEvent(UserEvent.SetUserName(it)) },
                        placeholder = { Text(text = "Your name") })
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(onClick = {
                        if (state.userName.isEmpty()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please enter a name")
                            }
                        } else {
                            onEvent(UserEvent.SaveUser)
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        )
    }
}