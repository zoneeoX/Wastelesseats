package app.wastelesseats.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import app.wastelesseats.R
import app.wastelesseats.nav.Screens
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = viewModel.loginState.collectAsState(initial = null)

    val textFieldShape = RoundedCornerShape(8.dp)
    val darkerGreen = Color(0xFF0CBC8B)
    val loginImage = R.drawable.login


    val lightTextColor = Color.Black
    val darkTextColor = Color.White
    val isDarkTheme = isSystemInDarkTheme()

    val textColor = if (isDarkTheme) darkTextColor else lightTextColor
    val backgroundColor = if (isDarkTheme) Color(30, 41, 59) else Color.White



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor),

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = loginImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(start = 20.dp)
                )
                Text("Login", fontSize = 35.sp, color = textColor, fontWeight = FontWeight.Bold, modifier = Modifier
                    .padding(bottom = 20.dp))
                Spacer(modifier = Modifier.height(8.dp))

            }






            Text("Enter your email and password to login", color = textColor)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = textColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),

                shape = textFieldShape
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = textColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),

                shape = textFieldShape
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.loginUser(email, password)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = darkerGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Don't have an account? Register",
                modifier = Modifier.clickable {
                    navController.navigate(Screens.RegisterScreen.route)
                },
                color = textColor
            )
        }
    }


    LaunchedEffect(key1 = state.value?.isSuccess){
        scope.launch{
            if(state.value?.isSuccess?.isNotEmpty() == true){
                val success = state.value?.isSuccess
                Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                navController.navigate(Screens.Home.route)

            }

        }
    }
    LaunchedEffect(key1 = state.value?.isError){
        scope.launch{
            if(state.value?.isError?.isNotEmpty() == true){
                val error = state.value?.isSuccess
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }
}