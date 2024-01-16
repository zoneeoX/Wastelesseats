package app.wastelesseats

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.wastelesseats.nav.NavGraph
import app.wastelesseats.ui.theme.WastelesseatsTheme
import app.wastelesseats.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import app.wastelesseats.nav.NavGraphWithBottomBar


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WastelesseatsTheme {
                val viewModel by viewModels<ImageClassificationViewModel>()
                var isImageSelectionDialogOpen by remember { mutableStateOf(false) }
                var isPredictionResultDialogOpen by remember { mutableStateOf(false) }

                val cameraLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview(),
                        onResult = { bitmapImage ->
                            viewModel.selectedImage = bitmapImage
                            viewModel.classifyImage()
                        })
                val mediaLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = { uri ->
                            val bitmapImage = uri?.let { viewModel.uriToBitmap(it) }
                            viewModel.selectedImage = bitmapImage
                            uri?.let{
                                viewModel.classifyImage()
                            }
                        })
                val cameraPermissionResultLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            if (isGranted) {
                                cameraLauncher.launch(null)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Camera permission required",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                if (isImageSelectionDialogOpen) {
                    viewModel.predictionResult = null
                    ImageSelectionDialog(
                        onOpenCamera = {
                            cameraPermissionResultLauncher.launch( Manifest.permission.CAMERA)
                            isPredictionResultDialogOpen = true
                        },

                        onOpenMedia = {
                            mediaLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            isPredictionResultDialogOpen = true},

                        ) { isImageSelectionDialogOpen = false }
                }
                if (isPredictionResultDialogOpen) {
                    viewModel.predictionResult?.let {
                        PredictionResultDialog(
                            predictionResult = it
                        ) { isPredictionResultDialogOpen = false }
                    }
                }


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    NavGraphWithBottomBar(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        onOpen = {isImageSelectionDialogOpen = true}
                    )
                }
            }
        }
    }
}


@Composable
fun ImageSelectionDialog(
    onOpenCamera: () -> Unit,
    onOpenMedia: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text("Select Image Source")
        },
        confirmButton = {
            Button(
                onClick = {
                    onOpenCamera()
                    onClose()
                }
            ) {
                Text("Open Camera")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onOpenMedia()
                    onClose()
                }
            ) {
                Text("Open Media")
            }
        }
    )
}

@Composable
fun PredictionResultDialog(
    predictionResult: String,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(predictionResult)
        },
        text = {
            Text("Would you like to sell it?")
        },
        confirmButton = {
            Button(
                onClick = onClose,
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = onClose,
            ) {
                Text("No")
            }
        }
    )
}

