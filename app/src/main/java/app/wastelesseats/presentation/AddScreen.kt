package app.wastelesseats.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController
import app.wastelesseats.nav.Screens
import app.wastelesseats.util.MarkerData
import app.wastelesseats.util.SharedViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun AddScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: MapsViewModel = viewModel(),
) {
    var userId by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    userId = currentUser?.uid.toString()

    val context = LocalContext.current

    var userUploadedItems by remember { mutableStateOf(listOf<MarkerData>()) }

    LaunchedEffect(userId) {
        sharedViewModel.getUserUploadedItems(userId) { items ->
            userUploadedItems = items
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(color = Color(250, 250, 250))

    )
    {
        Text(
            text = "Your Items",
            modifier = Modifier
                .padding(16.dp),
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            )

        if (userUploadedItems.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                userUploadedItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(5.dp))
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = Color.Gray,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            item.imageUrl.let { imageUrl ->
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
                                        .size(56.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        ) {
                            Text(text = item.title ?: "", fontWeight = FontWeight.Bold)
                            Text(
                                text = if (item.price == 0) "Free" else "Rp. ${item.price}",
                                color = if (item.price == 0) Color.Gray else Color.Black
                            )
                        }
                    }
                }

                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screens.AddScreenChild.route)
                    },
                    modifier = Modifier
                        .padding(16.dp, 16.dp, 16.dp, 50.dp)
                        .size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shopping Cart",
                    modifier = Modifier.size(120.dp)
                )
                Text(text = "No Donations Yet", modifier = Modifier.padding(top = 16.dp))

                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screens.AddScreenChild.route)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}


