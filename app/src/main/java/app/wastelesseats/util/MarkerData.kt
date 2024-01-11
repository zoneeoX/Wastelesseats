package app.wastelesseats.util

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
)
