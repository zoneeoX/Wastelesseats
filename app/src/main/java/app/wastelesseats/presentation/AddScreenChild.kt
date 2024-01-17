package app.wastelesseats.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.wastelesseats.nav.Screens
import app.wastelesseats.util.MarkerData
import app.wastelesseats.util.SharedViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date





@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreenChild(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: MapsViewModel = viewModel(),
) {
    var title: String by remember { mutableStateOf("") }
    var lat: Double by remember { mutableStateOf(0.0) }
    var lng: Double by remember { mutableStateOf(0.0) }
    var description: String by remember { mutableStateOf("") }
    var userId: String by remember { mutableStateOf("") }
    var price: Int by remember { mutableStateOf(0) }
    var categorys: String by remember { mutableStateOf("") }
    val selectedDateState = rememberDatePickerState(Calendar.getInstance().timeInMillis)


    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    userId = currentUser?.uid.toString()

    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(LocalContext.current)
    val locationPermissionRequestCode = 1001

    val confirmEnabled by remember {
        derivedStateOf {
            true
        }
    }

    val darkerGreen = Color(0xFF0CBC8B)


    LaunchedEffect(Unit) {
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val cameraPermission = Manifest.permission.CAMERA

        val locationPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            locationPermission
        ) == PackageManager.PERMISSION_GRANTED

        val cameraPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            cameraPermission
        ) == PackageManager.PERMISSION_GRANTED

        val permissionsToRequest = mutableListOf<String>()

        if (!locationPermissionGranted) {
            permissionsToRequest.add(locationPermission)
        }

        if (!cameraPermissionGranted) {
            permissionsToRequest.add(cameraPermission)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                permissionsToRequest.toTypedArray(),
                locationPermissionRequestCode
            )
        }
    }


    var capturedImage: ImageBitmap? by remember { mutableStateOf(null) }

    val takePictureLauncher: ActivityResultLauncher<Void?> =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
            if (result != null) {
                capturedImage = result.asImageBitmap()
            }
        }

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference




    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
            }
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = "Shopping Icon",
                modifier = Modifier
                    .size(48.dp)
            )
            Text(
                text = "Donate Tab",
                style = MaterialTheme.typography.headlineMedium
            )
            Box(Modifier.size(48.dp))
        }


        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            val itemCategories = listOf("","Food", "Beverages", "Vegetables", "Electronics", "Others")
            var selectedCategoryIndex by remember { mutableStateOf(0) }
            var openDropdown by remember { mutableStateOf(false) }


            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = itemCategories[selectedCategoryIndex],
                    onValueChange = {
                    },
                    label = { Text(text = "Item Category") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    readOnly = true,
                    keyboardActions = KeyboardActions(onNext = {

                    }),
                    trailingIcon = {
                        IconButton(onClick = { openDropdown = true }) {
                            Icon(imageVector = Icons.Default.Category, contentDescription = null)
                        }
                    }
                )

                if (openDropdown) {
                    DropdownMenu(
                        expanded = openDropdown,
                        onDismissRequest = { openDropdown = false }
                    ) {
                        itemCategories.forEachIndexed { index, category ->
                            DropdownMenuItem(onClick = {
                                selectedCategoryIndex = index
                                categorys = itemCategories[index]
                                openDropdown = false
                            }) {
                                Text(text = category)
                            }
                        }
                    }
                }
            }

            var openDialog by remember { mutableStateOf(false) }


            if (openDialog) {
                DatePickerDialog(
                    onDismissRequest = {
                        openDialog = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val selectedDateMillis = selectedDateState.selectedDateMillis
                                openDialog = false
                            },
                            enabled = confirmEnabled
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = selectedDateState)
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = SimpleDateFormat("MM/dd/yyyy").format(selectedDateState.selectedDateMillis),
                    onValueChange = {},
                    label = { Text(text = "Select Date") },
                    singleLine = true,
                    readOnly = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = { openDialog = true }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                        }
                    }
                )
            }





            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Item Title") }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                value = price.toString(),
                onValueChange = { newValue ->
                    price = newValue.toIntOrNull() ?: 0
                },
                label = { Text(text = "Item Price") },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Item Description") },
                placeholder = { Text(text = "Item Description") },
                maxLines = 8,
                singleLine = false,
            )

            Button(
                onClick = {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePictureLauncher.launch(null)
                },
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 20.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(139, 92, 246),
                    contentColor = Color.White
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Camera Icon",
                    modifier = Modifier
                        .scale(2f)
                        .size(48.dp),
                    tint = Color.White
                )
            }


            Button(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        color = darkerGreen,
                        shape = RoundedCornerShape(percent = 50)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkerGreen,
                    contentColor = Color.White
                ),
                onClick = {
                    try {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            location?.let {
                                val userLat = it.latitude
                                val userLng = it.longitude
                                lat = userLat
                                lng = userLng
                                val fireStoreRef = Firebase.firestore

                                capturedImage?.let { imageBitmap ->
                                    val baos = ByteArrayOutputStream()
                                    imageBitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                    val data = baos.toByteArray()
                                    val imageRef = storageRef.child("images/${fireStoreRef.collection("markers").document().id}.jpg")

                                    val uploadTask = imageRef.putBytes(data)
                                    uploadTask.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            imageRef.downloadUrl.addOnCompleteListener { urlTask ->
                                                if (urlTask.isSuccessful) {
                                                    val imageUrl = urlTask.result.toString()

                                                    val selectedDateMillis = selectedDateState.selectedDateMillis
                                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                                                    val formattedDate = dateFormat.format(selectedDateMillis)

                                                    val markerData = MarkerData(
                                                        id = fireStoreRef.collection("markers").document().id,
                                                        userId = userId,
                                                        title = title,
                                                        expired = formattedDate,
                                                        lat = lat,
                                                        lng = lng,
                                                        description = description,
                                                        price = price,
                                                        timestamp = Timestamp.now(),
                                                        category = categorys,
                                                        imageUrl = imageUrl
                                                    )
                                                    sharedViewModel.saveData(userData = markerData, context = context)
                                                } else {
                                                    // test
                                                }
                                            }
                                        } else {
                                            // test
                                        }
                                    }
                                }
                            }
                        }.addOnFailureListener { /* test */ }
                    } catch (e: SecurityException) {
                        // Security
                    } catch (e: Exception) {
                        // testing
                    }
                    navController.navigate(Screens.AddScreen.route)
                }
            ) {
                Text(text = "Add Product")
            }
        }
    }
}

