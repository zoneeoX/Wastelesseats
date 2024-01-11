package app.wastelesseats.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.wastelesseats.ImageClassificationViewModel
import app.wastelesseats.presentation.AddScreen
import app.wastelesseats.presentation.MapScreen
import app.wastelesseats.presentation.auth.LoginScreen
import app.wastelesseats.presentation.auth.RegisterScreen
import app.wastelesseats.util.SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    onOpen: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route
    ) {
        //Map / Main Screen
        composable(
            route = Screens.MapScreen.route
        ) {
            MapScreen(
                navController = navController,
                onOpen = onOpen,
                sharedViewModel = sharedViewModel,
            )
        }
        //Add Screen
        composable(
            route = Screens.AddScreen.route
        ) {
            AddScreen(navController = navController, sharedViewModel = sharedViewModel)
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