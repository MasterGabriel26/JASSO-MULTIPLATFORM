package com.jasso.inteligenciaenventas.android.AuthViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.jasso.inteligenciaenventas.states.AuthState
import com.jasso.inteligenciaenventas.viewModels.AuthViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RegisterUserAuth(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val authState = authViewModel.authState.collectAsState().value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center // Centramos el contenido dentro de la Box
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Definir un ancho relativo
                .padding(16.dp), // Añadir padding
            horizontalAlignment = Alignment.CenterHorizontally // Centrar el contenido en la columna
        ) {
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp)) // Separación entre los campos
            TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(8.dp)) // Separación entre los campos
            TextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation())

            Spacer(modifier = Modifier.height(16.dp)) // Separación entre los botones

            Button(onClick = {
                scope.launch {
                    authViewModel.signUp(email, password, confirmPassword)
                }
            }) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(8.dp)) // Separación entre botones

            Button(onClick = { onBack() }) {
                Text("Back to Login")
            }

            Spacer(modifier = Modifier.height(16.dp)) // Separación final

            when (authState) {
                is AuthState.Success -> Text("Welcome ${authState.user.email}")
                is AuthState.Error -> Text("Error: ${authState.message}")
                else -> {}
            }
        }
    }
}

