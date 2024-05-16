package com.example.dogadoption.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dogadoption.room.UserEvent
import com.example.dogadoption.room.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserDialog(
    state: UserState,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(UserEvent.HideDialog) },
        title = { Text(text = "What is your name?") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(value = state.userName,
                    onValueChange = {
                        onEvent(UserEvent.SetUserName(it))
                    },
                    placeholder = {
                        Text(text = "Your name")
                    })
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = { UserEvent.SaveUser }) {
                    Text(text = "Save")
                }
            }
        }
    )
}