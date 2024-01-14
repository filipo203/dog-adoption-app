package com.example.dogadoption

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.ColorSpace.Model
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.AlertDialogDefaults.titleContentColor
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.dogadoption.ui.theme.DogAdoptionTheme
import com.example.dogadoption.ui.theme.ImageCard
import com.example.dogadoption.ui.theme.LoadImageFromURL
import com.example.dogadoption.ui.theme.LoadImageFromURL2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter",
        "UnrememberedMutableState" // To juz chyba nie jest potrzebne
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            /**
             * `senderEmail` i `emailSubject` moga byc `const val` bo sie nigdy nie zmieniaja.
             * https://www.baeldung.com/kotlin/constants-best-practices
             */
            val senderEmail = remember {
                mutableStateOf("29filip@gmail.com")
            }
            val emailSubject = remember {
                mutableStateOf("Dog Adoption: Request") // FIXME: Zrob tak zeby to pochodzilo z resources
            }
            val ctx = LocalContext.current

            // FIXME: `result` nie urzywany
            val result = { "" }
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult()
            ) {
                /**
                 * Z tego co widze to jak wysyla sie email to result jest "0" co oznacza
                 * ze wysylanie bylo zcancelowane przez urzytkownika albo email app nie zwraca
                 * zadnego result. Wiec jak dostaniemy result mozemy pokazac snackbar z message
                 * "Email intent completed"
                 * TODO: zmienic message na "Email intent completed"
                 */
                scope.launch { snackbarHostState.showSnackbar("Email sent") } }

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
                                    "Dog Adoption", // FIXME: Zrob tak zeby to pochodzilo z resources
                                    maxLines = 1,
                                    fontSize = 36.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            })
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(state = rememberScrollState())
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.padding(34.dp))
                        Description("Description") // FIXME: Zrob tak zeby to pochodzilo z resources

                        val painter = painterResource(id = R.drawable.schnauzer)

                        /**
                         * Zamiast tworzyc `contentDescription` mozesz uzyc `title`
                         * jako `contentDescription`
                         */
                        val contentDescription = "Miko - Schnauzer"
                        val title = "Miko - Schnauzer" // FIXME: Zrob tak zeby to pochodzilo z resources

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

                        Button(onClick = {
                            val i = Intent(Intent.ACTION_SEND)
                            val emailAddress = arrayOf(senderEmail.value)
                            i.putExtra(Intent.EXTRA_EMAIL, emailAddress)
                            i.putExtra(Intent.EXTRA_SUBJECT, emailSubject.value)

                            i.setType("message/rfc822")

                            /**
                             * FIXME: w dalszym ciagu chcemy uzywac `launcher.launch()` z tym ze
                             * wymaga on zeby podac mu powyzszy `Intent` a nie `IntentSenderRequest`
                             * Mozesz podac w launch:
                             * `val chooser = Intent.createChooser(i, "Choose an Email client : ")`
                             */

                            //launcher.launch()

                            // To nie bedzie juz nam potrzebne
                            //scope.launch { snackbarHostState.showSnackbar("Email sent") }


                            // I to tez nie bedzie potrzebne. I jak to usuniesz to na gorze jest
                            // `val ctx = LocalContext.current` ktory tez nie bedzie potrzebny
                            ctx.startActivity(
                                Intent.createChooser(
                                    i, "Choose an Email client : "
                                )
                            )

                        })
                        {
                            Text(
                                // FIXME: Zrob tak zeby to pochodzilo z resources
                                // https://www.youtube.com/watch?v=7urBtGbF1-M&ab_channel=Kodeco
                                text = "Contact me",
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

@Composable
fun Description(
    name: String, // FIXME: Nie urzywane
    modifier: Modifier = Modifier
) {
    Text(
        text = "Adopt your new best friend!",  // FIXME: Zrob tak zeby to pochodzilo z resources
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(4.dp)
    )
}
