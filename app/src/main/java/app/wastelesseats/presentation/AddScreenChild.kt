package app.wastelesseats.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
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
    var title: String by remember { mutableStateOf("")}
    var lat: Double by remember { mutableStateOf(0.0)}
    var lng: Double by remember { mutableStateOf(0.0)}
    var description: String by remember { mutableStateOf("")}
    var userId: String by remember { mutableStateOf("")}
    var price: Int by remember { mutableStateOf(0)}
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
            // Add your validation logic here if needed
            // For example, you can check if the selected date is not null
            true
        }
    }

    LaunchedEffect(Unit) {
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            locationPermission
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(locationPermission),
                locationPermissionRequestCode
            )
        }
    }







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

            val itemCategories = listOf("Food", "Beverages", "Vegetables", "Electronics", "Others")
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
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
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
                maxLines = 8, // Increased number of lines
                singleLine = false,
            )



            Button(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                onClick = {
                    try {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            location?.let {
                                val userLat = it.latitude
                                val userLng = it.longitude
                                lat = userLat
                                lng = userLng
                                val fireStoreRef = Firebase.firestore

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
                                    category = categorys
                                )
                                sharedViewModel.saveData(userData = markerData, context = context)
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


