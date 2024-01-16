package app.wastelesseats.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.wastelesseats.util.MarkerData
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun BuyScreen(
    item: MarkerData,
    onBuyClick: (MarkerData) -> Unit,
    onClose: () -> Unit
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .background(color = Color.Gray, shape = CircleShape),
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
                        .size(160.dp)
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
        val expirationDate = item.expired ?: ""
        val daysRemaining = calculateDaysRemaining(expirationDate)

        Text(
            text = if (daysRemaining > 0) {
                "$daysRemaining days left before expired"
            } else {
                "Expired"
            },
            color = if (daysRemaining > 0) {
                Color.Black
            } else {
                Color.Red
            },
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Text(
            text = item.description ?: "",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Button(
            onClick = {
                onBuyClick.invoke(item)
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = darkerGreen,
                    shape = RoundedCornerShape(12.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = darkerGreen,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Buy",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
