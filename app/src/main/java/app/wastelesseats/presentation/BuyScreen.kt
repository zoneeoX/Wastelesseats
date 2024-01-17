package app.wastelesseats.presentation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.wastelesseats.nav.Screens
import app.wastelesseats.util.MarkerData
import app.wastelesseats.util.SharedViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun BuyScreen(
    item: MarkerData,
    onBuyClick: (MarkerData) -> Unit,
    onClose: () -> Unit,
    sharedViewModel: SharedViewModel,
    navController: NavController,
) {
    val darkerGreen = Color(0xFF0CBC8B)
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser?.uid
    val isCurrentUserItem = user == item.userId
    val context = LocalContext.current

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(250,250,250)
            )
    ) {



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(color = Color.Gray, shape = CircleShape)
                .shadow(
                    elevation = 16.dp,
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
                        .size(280.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = item.title ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (item.price == 0) "Free" else "Rp. ${item.price}",
            color = if (item.price == 0) Color.Gray else darkerGreen,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Item Description",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }

        val expirationDate = item.expired ?: ""
        val daysRemaining = calculateDaysRemaining(expirationDate)




            Text(
                text = item.description ?: "",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Item Date",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = if (daysRemaining > 0) {
                "$daysRemaining days left before expired (${item.expired})"
            } else {
                "Expired"
            },
            color = if (daysRemaining > 0) {
                Color.Black
            } else {
                Color.Red
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (isCurrentUserItem) {
            Button(
                onClick = {
                    if (item.status == "Available") {
                        sharedViewModel.initiateBuy(
                            item,
                            onSuccess = {
                                Toast.makeText(context, "Buy initiated successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screens.Home.route)
                            })
                    } else {
                        Toast.makeText(context, "Buy initiated failed", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = false,

                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        color = if (item.status == "Available") darkerGreen else Color.Gray,
                        shape = RoundedCornerShape(percent = 50)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (item.status == "Available") darkerGreen else Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "This item is yours",
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }
        } else {
            Button(
                onClick = {
                    if (item.status == "Available") {
                        sharedViewModel.initiateBuy(
                            item,
                            onSuccess = {
                                Toast.makeText(context, "Buy initiated successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screens.Home.route)
                            })
                    } else {
                      Toast.makeText(context, "Buy initiated failed", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = item.status == "Available",

                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        color = if (item.status == "Available") darkerGreen else Color.Gray,
                        shape = RoundedCornerShape(percent = 50)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (item.status == "Available") darkerGreen else Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = when (item.status) {
                        "Available" -> "Buy"
                        "Pending" -> "Item Is Pending"
                        else -> "Item Sold"
                    },
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }
        }
    }
    }

}
