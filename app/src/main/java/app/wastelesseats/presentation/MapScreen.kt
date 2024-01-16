package app.wastelesseats.presentation
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.wastelesseats.util.MarkerData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.wastelesseats.R
import app.wastelesseats.util.SharedViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Date



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerInfoBox(
    markerData: MarkerData,
    onClose: () -> Unit,
    currentUserId: String,
    onBuyClick: (MarkerData) -> Unit
) {
    val darkerGreen = Color(0xFF0CBC8B)

    @SuppressLint("SimpleDateFormat")
    fun calculateDaysRemaining(expirationDate: String): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = Date()

        try {
            val expiredDate = dateFormat.parse(expirationDate)
            val difference = expiredDate!!.time - currentDate.time
            return (difference / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }


    ModalBottomSheet(
        onDismissRequest = onClose
    ) {
        val isCurrentUserItem = markerData.userId == currentUserId

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(color = Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                markerData.imageUrl?.let { imageUrl ->
                    val painter = rememberImagePainter(
                        data = imageUrl,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    )

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = markerData.title ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if(markerData.price == 0) "Free" else "Rp. ${markerData.price}",
                color = if (markerData.price == 0) Color.Gray else darkerGreen,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Item Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            val expirationDate = markerData.expired ?: ""
            val daysRemaining = calculateDaysRemaining(expirationDate)

            Text(
                text = if (daysRemaining > 0) {
                    "$daysRemaining days left before expired"
                } else {
                    "Expired"
                },
                color = if(daysRemaining > 0){
                                             Color.Black
                                             } else {
                                                    Color.Red
                                                    },
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Text(
                text = markerData.description ?: "",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (isCurrentUserItem) {
                Text(
                    text = "This is your item",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else {
                Button(
                    onClick = {
                        if (markerData.status == "Available") {
                            onBuyClick.invoke(markerData)
                        } else {
                            //test
                        }
                    },
                    enabled = markerData.status == "Available",

                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            color = if (markerData.status == "Available") darkerGreen else Color.Gray, // Use gray background for disabled button
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (markerData.status == "Available") darkerGreen else Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = when (markerData.status) {
                            "Available" -> "Buy"
                            "Pending" -> "Item Is Pending"
                            else -> "Item Sold"
                        },
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun ClusterMarkerDialog(
    markers: List<MarkerData>,
    onClose: () -> Unit,
    currentUserId: String,
    onMarkerClicked: (MarkerData) -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                text = "Multiple Items",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        },
        text = {
            Column {
                markers.forEachIndexed { index, marker ->
                    if (index > 0) {
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                    Row(
                        modifier = Modifier.clickable {
                            onMarkerClicked(marker)
                            onClose()
                        }
                    ) {
                        Text(
                            text = "${index + 1}. ${marker.title ?: ""}",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {}
    )
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    width: Int,
    height: Int
): BitmapDescriptor? {
    return try {
        val drawable = ContextCompat.getDrawable(context, vectorResId)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas)
        BitmapDescriptorFactory.fromBitmap(bitmap)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@Composable
fun MapScreen(
    viewModel: MapsViewModel = viewModel(),
    navController: NavController,
    onOpen: () -> Unit,
    sharedViewModel: SharedViewModel,
) {
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    val firestore = FirebaseFirestore.getInstance()
    val collectionRef = firestore.collection("markers")
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid
    val context = LocalContext.current
    val iconResourceId = R.drawable.location_icon
    val iconResourceIdMultiple = R.drawable.multiple
    val icon = bitmapDescriptorFromVector(context,iconResourceId,75,75)
    val iconMultiple = bitmapDescriptorFromVector(context,iconResourceIdMultiple,75,75)
    val cameraPositionState = rememberCameraPositionState()

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



    val listener = remember {
        collectionRef.addSnapshotListener { snapshot, _ ->
            val updatedMarkers = mutableListOf<MarkerData>()

            snapshot?.documents?.forEach { document ->
                val id = document.getString("id") ?: ""
                val userId = document.getString("userId") ?: ""
                val title = document.getString("title") ?: ""
                val expired = document.getString("expired") ?: ""
                val lat = document.getDouble("lat") ?: 0.0
                val lng = document.getDouble("lng") ?: 0.0
                val description = document.getString("description") ?: ""
                val price = document.getLong("price")?.toInt() ?: 0
                val status = document.getString("status")?: ""
                val imageUrl = document.getString("imageUrl")?: ""
                val category = document.getString("category")?: ""
                val timestamp = document.getTimestamp("timestamp")?: null
                val marker = MarkerData(id, userId, title, expired, lat, lng, description, price, status, timestamp, category, imageUrl)
                updatedMarkers.add(marker)
            }

            viewModel.setMarkers(updatedMarkers)
        }
    }

    DisposableEffect(key1 = viewModel) {
        onDispose {
            listener.remove()
        }
    }

    Scaffold(
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .padding(top = 28.dp, end = 10.dp)
                    .fillMaxSize()
            ) {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(MapEvent.ToggleDarkMode) },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (viewModel.state.isDarkMode) {
                            Icons.Default.WbSunny
                        } else Icons.Default.ModeNight,
                        contentDescription = "Toggle Dark Mode"
                    )
                }
                FloatingActionButton(
                    onClick = { onOpen() },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Add"
                    )
                }
            }
        }
    ){
        var selectedMarker by remember { mutableStateOf<MarkerData?>(null) }
        var selectedMarkerForDetails by remember { mutableStateOf<MarkerData?>(null) }
        var showDialog by remember { mutableStateOf(false) }
        var selectedMarkers by remember { mutableStateOf(emptyList<MarkerData>()) }




        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = viewModel.state.properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,

        ) {
            val markerLocations = mutableMapOf<Pair<Double, Double>, List<MarkerData>>()

            fun onClusterMarkerClicked(markers: List<MarkerData>) {
                showDialog = true
                selectedMarkers = markers
            }

            viewModel.markersFromFirestore.value?.forEach { marker ->
                val markerLocation = Pair(marker.lat, marker.lng)

                if (markerLocation in markerLocations) {

                    val updatedMarkers = markerLocations.getValue(markerLocation).toMutableList()
                    updatedMarkers.add(marker)
                    markerLocations[markerLocation] = updatedMarkers
                } else {

                    markerLocations[markerLocation] = listOf(marker)
                }
            }

            markerLocations.forEach { (location, markers) ->
                val position = LatLng(location.first, location.second)

                if (markers.size > 1) {

                    Marker(
                        position = position,
                        title = "Multiple Items",
                        snippet = "${markers.size} items",
                        icon = iconMultiple,
                        onClick = {
                            onClusterMarkerClicked(markers)
                            true
                        }
                    )
                } else {
                    val singleMarker = markers.first()
                    Marker(
                        position = position,
                        title = singleMarker.title,
                        snippet = singleMarker.description,
                        icon = icon,
                        onClick = {
                            selectedMarker = singleMarker
                            true
                        }
                    )
                }
            }

            if (showDialog) {
                ClusterMarkerDialog(
                    markers = selectedMarkers,
                    onClose = { showDialog = false },
                    currentUserId = currentUserId.toString()
                ) { marker ->
                    selectedMarkerForDetails = marker
                }
            }

            if (selectedMarker != null) {
                MarkerInfoBox(
                    markerData = selectedMarker!!,
                    onClose = { selectedMarker = null },
                    currentUserId = currentUserId.toString(),
                    onBuyClick = { marker ->
                        sharedViewModel.initiateBuy(
                            marker,
                            onSuccess = {
                                Toast.makeText(context, "Buy initiated successfully", Toast.LENGTH_SHORT).show()
                            },

                        )
                    }
                )
            }

            if (selectedMarkerForDetails != null) {
                MarkerInfoBox(
                    markerData = selectedMarkerForDetails!!,
                    onClose = { selectedMarkerForDetails = null },
                    currentUserId = currentUserId.toString(),
                    onBuyClick = { marker ->
                        sharedViewModel.initiateBuy(
                            marker,
                            onSuccess = {
                                Toast.makeText(context, "Buy initiated successfully", Toast.LENGTH_SHORT).show()
                            },

                        )
                    }
                )
            }
        }


    }
}

