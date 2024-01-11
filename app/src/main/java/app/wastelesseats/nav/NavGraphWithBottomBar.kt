package app.wastelesseats.nav

import NotificationScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.wastelesseats.presentation.AddScreen
import app.wastelesseats.presentation.AddScreenChild
import app.wastelesseats.presentation.MapScreen
import app.wastelesseats.presentation.auth.LoginScreen
import app.wastelesseats.presentation.auth.RegisterScreen
import app.wastelesseats.util.SharedViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraphWithBottomBar(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    onOpen: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.LoginScreen.route
        ) {
            composable(
                route = Screens.MapScreen.route
            ) {
                MapScreen(
                    navController = navController,
                    onOpen = onOpen,
                    sharedViewModel = sharedViewModel
                )
            }
            //Add Screen
            composable(
                route = Screens.NotificationScreen.route
            ) {
                NotificationScreen(navController = navController, sharedViewModel = sharedViewModel)
            }

            composable(
                route = Screens.AddScreen.route
            ) {
                AddScreen(navController = navController, sharedViewModel = sharedViewModel)
            }
            composable(
                route = Screens.AddScreenChild.route
            ) {
                AddScreenChild(navController = navController, sharedViewModel = sharedViewModel)
            }
            //Login Screen
            composable(
                route = Screens.LoginScreen.route
            ) {
                LoginScreen(navController = navController)
            }
            composable(
                route = Screens.RegisterScreen.route
            ) {
                RegisterScreen(navController = navController)
            }
        }
    }
}

