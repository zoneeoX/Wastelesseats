package app.wastelesseats.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.navArgument
import app.wastelesseats.presentation.Category

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


    object Home : Screens(
        route = "Home",
        title = "Home",
        icon = Icons.Default.Home
    )


    object AddScreenChild : Screens(
        route = "add_screen_child",
        title = "Upload",
        icon = null
    )

    object ProfileScreen : Screens(
        route = "profile_screen",
        title = "Profile",
        icon = Icons.Default.Person
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
    object CategoryItemsScreen : Screens(
        route = "category_items/{category}",
        title = "Category Items",
        icon = null
    )
    object NotificationScreen : Screens(
        route = "notification_screen",
        title = "Notification",
        icon = Icons.Default.Notifications
    )
}

fun categoryItemsScreenRoute(navController: NavController, category: Category) {
    val categoryRoute = "category_items/${category.name}"
    navController.navigate(categoryRoute)
}

