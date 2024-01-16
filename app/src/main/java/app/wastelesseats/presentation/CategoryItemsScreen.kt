package app.wastelesseats.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.wastelesseats.util.MarkerData
import app.wastelesseats.util.SharedViewModel

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


    Column {
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