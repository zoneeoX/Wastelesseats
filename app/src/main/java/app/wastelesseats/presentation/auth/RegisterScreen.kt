package app.wastelesseats.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.wastelesseats.R
import app.wastelesseats.nav.Screens
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val state = viewModel.registerState.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val darkerGreen = Color(0xFF0CBC8B)
    val registerImage = R.drawable.register
    val lightTextColor = Color.Black
    val darkTextColor = Color.White
    val isDarkTheme = isSystemInDarkTheme()

    val textColor = if (isDarkTheme) darkTextColor else lightTextColor
    val backgroundColor = if (isDarkTheme) Color(30, 41, 59) else Color.White

    val textFieldShape = RoundedCornerShape(8.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = registerImage),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(start = 20.dp)
            )
            Text("Register", fontSize = 35.sp, color = textColor, fontWeight = FontWeight.Bold, modifier = Modifier
                .padding(bottom = 20.dp))
            Spacer(modifier = Modifier.height(8.dp))

        }

        Text("Please fill in the details to register")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = textFieldShape
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = textFieldShape
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.registerUser(email, password)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = darkerGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Already have an account? Login",
            modifier = Modifier.clickable {
                navController.navigate(Screens.LoginScreen.route)
            }
        )
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
