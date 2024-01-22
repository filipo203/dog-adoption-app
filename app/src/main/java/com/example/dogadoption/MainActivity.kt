package com.example.dogadoption

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dogadoption.ui.theme.DogAdoptionTheme
import com.example.dogadoption.ui.theme.ImageCard
import com.example.dogadoption.ui.theme.LoadImageFromURL
import com.example.dogadoption.ui.theme.LoadImageFromURL2
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import kotlinx.coroutines.coroutineScope

const val senderEmail = ("29filip@gmail.com")


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter",
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val painter = painterResource(id = R.drawable.schnauzer)

            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            val emailSubject = stringResource(R.string.email_subject)
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode ==
                    AppCompatActivity.RESULT_OK
                ) {
                    scope.launch { snackbarHostState.showSnackbar("Email intent completed") }
                }
            }

            DogAdoptionTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(
                                    stringResource(R.string.app_name),
                                    maxLines = 1,
                                    fontSize = 36.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(state = rememberScrollState())
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.padding(34.dp))
                        Text(
                            stringResource(R.string.description),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(4.dp)
                        )

                        ImageCard(painter)
                        LoadImageFromURL()
                        LoadImageFromURL2()

                        Button(onClick = {
                            val i = Intent(Intent.ACTION_SEND)
                            val emailAddress = arrayOf(senderEmail)
                            i.putExtra(Intent.EXTRA_EMAIL, emailAddress)
                            i.putExtra(Intent.EXTRA_SUBJECT, emailSubject)

                            i.setType("message/rfc822")

                            /* TODO: "Handle send email result and show snackbar message" */
                            launcher.launch(i)
                        })
                        {
                            Text(
                                stringResource(id = R.string.button),
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}

