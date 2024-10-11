package com.jasso.inteligenciaenventas.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.jasso.inteligenciaenventas.android.AuthViews.LoginUserAuth
import com.jasso.inteligenciaenventas.android.AuthViews.RegisterUserAuth
import com.jasso.inteligenciaenventas.android.Homes.HomeActivity
import com.jasso.inteligenciaenventas.repositories.FirebaseAuthRepository
import com.jasso.inteligenciaenventas.states.AuthState
import com.jasso.inteligenciaenventas.viewModels.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        val authRepository = FirebaseAuthRepository()
        val authViewModel = AuthViewModel(authRepository)

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigator(authViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigator(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState = authViewModel.authState.collectAsState().value

    // Observa los cambios en el estado de autenticación y navega de acuerdo al estado del usuario
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true } // Limpia el stack para no regresar a login
                }
            }
            is AuthState.Error, AuthState.Empty -> {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    // Configuración del NavHost
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginUserAuth(authViewModel, navController) {
                navController.navigate("register")
            }
        }
        composable("register") {
            RegisterUserAuth(authViewModel) {
                navController.navigate("login")
            }
        }
        composable("home") {
            HomeActivity(navController) {
                authViewModel.logout() // Llamar al método logout del ViewModel
            }
        }
        composable("userScreen") {
            UserScreen()
        }
    }
}

@Composable
fun UserScreen() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = "Bienvenido, usuario")
    }
}

// Eliminar el composable SplashScreen completamente



@Composable
fun SplashScreen() {
    // Crear una pantalla simple de bienvenida
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Bienvenido a Jasso Ventas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        SplashScreen()
    }
}
