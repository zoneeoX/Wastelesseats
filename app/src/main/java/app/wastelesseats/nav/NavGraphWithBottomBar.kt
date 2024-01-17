package app.wastelesseats.nav

import NotificationScreen
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import app.wastelesseats.presentation.AddScreen
import app.wastelesseats.presentation.AddScreenChild
import app.wastelesseats.presentation.BuyScreen
import app.wastelesseats.presentation.CategoryItemsScreen
import app.wastelesseats.presentation.Home
import app.wastelesseats.presentation.MapScreen
import app.wastelesseats.presentation.auth.LoginScreen
import app.wastelesseats.presentation.auth.ProfileScreen
import app.wastelesseats.presentation.auth.RegisterScreen
import app.wastelesseats.util.MarkerData
import app.wastelesseats.util.SharedViewModel
@Composable
fun BuyScreenContent(itemId: String?, navController: NavController) {
    val sharedViewModel: SharedViewModel = viewModel()
    val itemState = remember(itemId) { mutableStateOf<MarkerData?>(null) }

    LaunchedEffect(itemId) {
        sharedViewModel.getItemById(itemId!!) { result ->
            result?.let {
                itemState.value = it
            } ?: run {
                //
            }
        }
    }

    itemState.value?.let {
        BuyScreen(item = it, onBuyClick = {}, onClose = {}, sharedViewModel = sharedViewModel, navController = navController)
    } ?: run {
        //
    }
}
@RequiresApi(Build.VERSION_CODES.O)
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
                route = "category_items/{category}",
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { entry ->
                val category = entry.arguments?.getString("category")
                CategoryItemsScreen(navController = navController, category = category!!,sharedViewModel = sharedViewModel)
            }


            composable(
                route = "buy/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) { entry ->
                val itemId = entry.arguments?.getString("itemId")

                val itemState = rememberUpdatedState(itemId)

                BuyScreenContent(itemId = itemState.value, navController = navController)
            }



            composable(
                route = Screens.Home.route
            ) {
                Home(navController = navController, sharedViewModel = sharedViewModel)
            }

            composable(
                route = Screens.ProfileScreen.route
            ) {
                ProfileScreen(navController = navController, sharedViewModel = sharedViewModel)
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

