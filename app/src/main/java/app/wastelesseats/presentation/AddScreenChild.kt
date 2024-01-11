package app.wastelesseats.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun AddScreenChild(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: MapsViewModel = viewModel(),
) {
    var title: String by remember { mutableStateOf("")}
    var expired: String by remember { mutableStateOf("")}
    var lat: Double by remember { mutableStateOf(0.0)}
    var lng: Double by remember { mutableStateOf(0.0)}
    var description: String by remember { mutableStateOf("")}
    var userId: String by remember { mutableStateOf("")}
    var price: Int by remember { mutableStateOf(0)}

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    userId = currentUser?.uid.toString()

    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(LocalContext.current)
    val locationPermissionRequestCode = 1001


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



    Column(modifier = Modifier.fillMaxSize()) {
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

            OutlinedTextField(
                modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Item Title") }
            )
            OutlinedTextField(
                modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
                value = expired,
                onValueChange = { expired = it },
                label = { Text(text = "Expiration Date") },
                placeholder = { Text(text = "YYYY/MM/DD")}
            )
            OutlinedTextField(
                modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
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



                                val markerData = MarkerData(
                                    id = fireStoreRef.collection("markers").document().id,
                                    userId = userId,
                                    title = title,
                                    expired = expired,
                                    lat = lat,
                                    lng = lng,
                                    description = description,
                                    price = price,
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


