package com.example.dogadoption

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.dogadoption.ui.theme.DogAdoptionTheme
import com.example.dogadoption.ui.theme.ImageCard
import com.example.dogadoption.ui.theme.LoadImageFromURL
import com.example.dogadoption.ui.theme.LoadImageFromURL2
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogAdoptionTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Header("Dog Adoption")
                            Column(
                                modifier = Modifier
                                    .verticalScroll(state = rememberScrollState())
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Description("Description")

                                val painter = painterResource(id = R.drawable.schnauzer)
                                val contentDescription = "Miko - Schnauzer"
                                val title = "Miko - Schnauzer"
                                ImageCard(
                                    painter = painter,
                                    contentDescription = contentDescription,
                                    title = title
                                )
                                LoadImageFromURL(
                                    painter = painter,
                                    contentDescription = contentDescription,
                                    title = title
                                )
                                LoadImageFromURL2(
                                    painter = painter,
                                    contentDescription = contentDescription,
                                    title = title
                                )
                                ContactMeButton()
                                Spacer(modifier = Modifier.padding(8.dp))

                            }

                        }

                    })
            }
        }
    }

    @Composable
    private fun Header(
        name: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            text = "Dog Adoption",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        )
    }

    @Composable
    fun Description(
        name: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            text = "Adopt your new best friend!",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(2.dp)
        )
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ContactMeButton(modifier: Modifier = Modifier) {

        val senderEmail = remember {
            mutableStateOf("29filip@gmail.com")
        }
        val emailSubject = remember {
            mutableStateOf("Dog Adoption: Request")
        }
        val ctx = LocalContext.current

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
//    "What I tried before"
//    Scaffold(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(80.dp),
//        snackbarHost = { SnackbarHost(snackbarHostState) },
//        content = {
        Button(onClick = {
            scope.launch {
                snackbarHostState.showSnackbar(message = "Email Sent")
            }

            val i = Intent(Intent.ACTION_SEND)
            val emailAddress = arrayOf(senderEmail.value)
            i.putExtra(Intent.EXTRA_EMAIL, emailAddress)
            i.putExtra(Intent.EXTRA_SUBJECT, emailSubject.value)

            i.setType("message/rfc822")

            ctx.startActivity(Intent.createChooser(i, "Choose an Email client : "))
        }) {
            Text(
                text = "Contact me",
                color = Color.White,
                fontSize = 18.sp
            )
        }
     }
// }
}

