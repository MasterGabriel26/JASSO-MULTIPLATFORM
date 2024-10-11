package com.jasso.inteligenciaenventas.android.AuthViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jasso.inteligenciaenventas.android.Homes.HomeActivity
import com.jasso.inteligenciaenventas.states.AuthState
import com.jasso.inteligenciaenventas.viewModels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginUserAuth(
    authViewModel: AuthViewModel,
    navController: NavController,
    onNavigateToRegister: () -> Unit
) {
    val authState = authViewModel.authState.collectAsState().value
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            isLoading = false
            navController.navigate("home")
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp)) // Separación entre los campos
            TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
            Spacer(modifier = Modifier.height(16.dp)) // Separación entre los botones

            if (isLoading) {
                CircularProgressIndicator() // Mostrar un indicador de carga mientras se procesa la autenticación
            } else {
                Button(onClick = {
                    scope.launch {
                        isLoading = true // Activar el estado de carga
                        authViewModel.signIn(email, password)
                    }
                }) {
                    Text("Sign In")
                }
            }


            Spacer(modifier = Modifier.height(8.dp)) // Separación entre botones

            Button(onClick = { onNavigateToRegister() }) {
                Text("Go to Register")
            }

            Spacer(modifier = Modifier.height(16.dp)) // Separación final

            when (authState) {
                is AuthState.Success -> {
                    // Navegar a la pantalla principal usando NavController
                    navController.navigate("home")
                }
//                is AuthState.Success -> Text("Welcome ${authState.user.email}")
                is AuthState.Error -> Text("Error: ${authState.message}")
                else -> {}
            }
        }
    }
}
