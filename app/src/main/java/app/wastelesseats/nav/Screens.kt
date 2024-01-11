package app.wastelesseats.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String, val icon: ImageVector?) {
    object MapScreen : Screens(
        route = "map_screen",
        title = "Map",
        icon = Icons.Default.Map
    )

    object AddScreen : Screens(
        route = "add_screen",
        title = "Donate",
        icon = Icons.Default.ShoppingBag
    )

    object AddScreenChild : Screens(
        route = "add_screen_child",
        title = "Upload",
        icon = null
    )

    object LoginScreen : Screens(
        route = "login_screen",
        title = "Login",
        icon = null
    )

    object RegisterScreen : Screens(
        route = "register_screen",
        title = "Register",
        icon = null
    )

    object NotificationScreen : Screens(
        route = "notification_screen",
        title = "Notification",
        icon = Icons.Default.Notifications
    )
}

