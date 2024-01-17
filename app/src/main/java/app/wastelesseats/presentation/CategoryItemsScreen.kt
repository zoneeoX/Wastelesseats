package app.wastelesseats.presentation

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.wastelesseats.util.MarkerData
import app.wastelesseats.util.SharedViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryItemsScreen(
    navController: NavController,
    category: String,
    sharedViewModel: SharedViewModel,
) {
    LaunchedEffect(category) {
        sharedViewModel.getMarkersByCategory(category)
    }

    val categoryItemsState by sharedViewModel.categoryItems.collectAsState()
    val categoryItems = categoryItemsState ?: emptyList()


    Column(
        Modifier.
        background(color = Color(250,250,250))
    ) {
        Text(
            text = category, modifier = Modifier
                .padding(20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(categoryItems.size) { index ->
                CategoryItem(item = categoryItems[index]) {
                    navController.navigate("buy/${categoryItems[index].id}")
                }
            }
        }

    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryItem(
    item: MarkerData,
    onItemClick: () -> Unit,
) {
    val darkerGreen = Color(0xFF0CBC8B)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(5.dp))
            .clickable(onClick = onItemClick),

        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                .padding(16.dp)

        ) {
            Column {
                val expirationDate = item.expired
                val daysRemaining = calculateDaysRemaining(expirationDate)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
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
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                            ,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = item.category,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }



                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {


                        Text(
                            text = if(item.price > 0)"Rp. ${item.price}" else "Free",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = if (daysRemaining > 0) {
                                "$daysRemaining days before expired"
                            } else {
                                "Expired"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (daysRemaining > 0) Color.Gray else Color.Red
                        )

                    }
                }

                /*Text(
                    text = if (daysRemaining > 0) {
                        "$daysRemaining days left before expired"
                    } else {
                        "Expired"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (daysRemaining > 0) Color.White else Color.Red
                )*/
            }
        }
    }
}


/*
@Composable
fun CategoryItem(item: MarkerData, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Display item details here
            Text(text = "Title: ${item.title}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Description: ${item.description}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Price: ${item.price}", style = MaterialTheme.typography.bodySmall)
            // Add more details if needed
        }
    }
}
*/
