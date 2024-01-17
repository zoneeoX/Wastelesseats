import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import app.wastelesseats.util.MarkerData
import app.wastelesseats.util.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.wastelesseats.nav.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val userId by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.uid) }
    val queryPending = remember { FirebaseFirestore.getInstance().collection("markers").whereEqualTo("userId", userId).whereEqualTo("status", "Pending") }
    val querySold = remember { FirebaseFirestore.getInstance().collection("markers").whereEqualTo("userId", userId).whereEqualTo("status", "Sold") }
    var confirmationTrigger by remember { mutableStateOf(false) }


    var pendingMarkers by remember { mutableStateOf(emptyList<MarkerData>()) }
    var soldMarkers by remember { mutableStateOf(emptyList<MarkerData>()) }




    LaunchedEffect(queryPending, querySold) {
        sharedViewModel.getMarkers(queryPending) { markerDataList ->
            pendingMarkers = markerDataList
        }
        sharedViewModel.getMarkers(querySold) { markerDataList ->
            soldMarkers = markerDataList
        }

        confirmationTrigger = !confirmationTrigger

    }





    Scaffold(
        modifier = Modifier,
        containerColor = Color(250,250,250),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(250,250,250)
                ),
                title = { Text(
                    text = "Notifications",
                    modifier = Modifier
                        .padding(0.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    style = MaterialTheme.typography.headlineMedium,
                    ) },
            )
        },
        content = {
            LazyColumn(
                contentPadding = PaddingValues(top = 72.dp, bottom = 72.dp)
            ) {
                item {
                    Text(text = "Pending", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(16.dp))
                }
                itemsIndexed(pendingMarkers) { index, marker ->
                    NotificationItem(
                        marker = marker,
                        sharedViewModel = sharedViewModel,
                        onUpdate = {
                            pendingMarkers = pendingMarkers.filter { it.id != marker.id }
                            navController.navigate(Screens.NotificationScreen.route)
                        }
                    )
                }

                item {
                    Text(text = "Completed", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(16.dp))
                }
                itemsIndexed(soldMarkers) { index, marker ->
                    NotificationItem(
                        marker = marker,
                        sharedViewModel = sharedViewModel,
                        onUpdate = {
                            soldMarkers = soldMarkers.filter { it.id != marker.id }
                            navController.navigate(Screens.NotificationScreen.route)
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun NotificationItem(marker: MarkerData, sharedViewModel: SharedViewModel, onUpdate: () -> Unit) {
    val darkerGreen = Color(0xFF0CBC8B)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row {
                Text(text = marker.title ?: "", fontWeight = FontWeight.Bold)
                Text(text = if(marker.price > 0)" (Rp. ${marker.price})" else " (Free)")
            }
            Text(text = marker.category ?: "")
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (marker.status == "Pending") Color.Gray else Color.Red,
                            shape = RoundedCornerShape(percent = 50)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = if (marker.status == "Pending") "Pending" else "Sold",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        )
                }

                if (marker.status == "Pending") {
                    // Confirm button
                    Button(
                        onClick = {
                            sharedViewModel.updateStatusToSold(marker.id)
                            onUpdate()

                        },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = darkerGreen),
                    ) {
                        Text(text = "Confirm")
                    }
                } else if (marker.status == "Sold") {
                    Button(
                        onClick = {
                            sharedViewModel.deleteItem(marker.id)
                            onUpdate()

                        },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Red),

                        ) {
                        Text(text = "Delete Data")
                    }
                }
            }
        }
    }
}
