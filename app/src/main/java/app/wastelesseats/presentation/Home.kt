package app.wastelesseats.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.wastelesseats.R
import app.wastelesseats.nav.Screens
import app.wastelesseats.nav.categoryItemsScreenRoute
import app.wastelesseats.util.MarkerData
import app.wastelesseats.util.SharedViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


data class Category(val name: String, val imageResource: Int)

val categories = listOf(
    Category("Food", R.drawable.food),
    Category("Beverages", R.drawable.beverage),
    Category("Vegetables", R.drawable.vegetable),
    Category("Electronics", R.drawable.electronics),
    Category("Others", R.drawable.others)
)


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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecentItem(
    markerData: MarkerData,
    navController: NavController,
    onClickCategory: (String) -> Unit,
    onClickBuy: (String) -> Unit
) {
    val darkerGreen = Color(0xFF0CBC8B)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(5.dp))
            .clickable {
                onClickBuy(markerData.id)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                .padding(16.dp)

        ) {
            Column {
                val expirationDate = markerData.expired
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
                        markerData.imageUrl.let { imageUrl ->
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
                            text = markerData.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = markerData.category,
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
                                text = if(markerData.price > 0)"Rp. ${markerData.price}" else "Free",
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(Unit) {
        sharedViewModel.getRecentlyAddedItems()
    }

    val recentItemsState: StateFlow<List<MarkerData>> = sharedViewModel.recentlyAddedItems
    val recentItems by recentItemsState.collectAsState()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(250,250,250))
    ){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp, 16.dp, 16.dp, 50.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "Map icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Current Location", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Jl. Soekarno Hatta 15A...", style = MaterialTheme.typography.bodySmall)


            }
        }


        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(16.dp)
                ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            placeholder = {
                Text(text = "Search Menu, restaurant or etc")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.FilterAlt, contentDescription = "Filter icon")
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF8BC34A),
                            Color(0xFF4CAF50)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Claim your",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Discount 30%",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "daily now",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 15.dp)
                )
                Button(
                    onClick = { /*  */ },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Gray
                    )
                ) {
                    Text(text = "Claim Promo")
                }
            }
        }
        Text(
            text = "Categories",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 10.dp)
        )
        LazyRow {
            items(categories) { category ->
                CategoryItem(category = category){
                    categoryItemsScreenRoute(navController, category)
                }
            }
        }

        Text(
            text = "Map View",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 10.dp)
        )

        Button(
            onClick = {
                navController.navigate(Screens.MapScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(139, 92, 246)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Map, contentDescription = "Map icon")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Map")
            }
        }
        Text(
            text = "Recent Items",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 10.dp)
        )

        if (recentItems.isEmpty()) {
            // TODO: nnti add aja muter muteran
        } else {
            for (item in recentItems) {
                RecentItem(
                    markerData = item,
                    navController = navController,
                    onClickCategory = {
                    },
                    onClickBuy = {
                        navController.navigate("buy/$it")
                    }
                )
            }
        }
    }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color = Color(0xFF66BB6A), shape = CircleShape)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = category.imageResource),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = category.name, style = MaterialTheme.typography.bodySmall)
    }
}

