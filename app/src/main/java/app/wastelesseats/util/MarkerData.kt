package app.wastelesseats.util

import android.os.Parcelable
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import com.google.firebase.Timestamp


data class MarkerData(
    var id: String = "",
    val userId: String = "",
    var title: String = "",
    var expired: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    val description: String = "",
    val price: Int = 0,
    val status: String = "Available",
    val timestamp: Timestamp? = null,
    val category: String = "",
    val imageUrl: String = "",

)
