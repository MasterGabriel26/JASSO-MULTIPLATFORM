package com.jasso.inteligenciaenventas.android.Homes


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jasso.inteligenciaenventas.models.ProspectoModel
import com.jasso.inteligenciaenventas.repositories.ProspectosRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.jasso.inteligenciaenventas.android.Homes.Screens.CalendarScreen
import com.jasso.inteligenciaenventas.android.Homes.Screens.HomeScreen
import com.jasso.inteligenciaenventas.android.formularios.ProspectoForm
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(navController: NavController, onLogout: () -> Unit) {
    val scope = rememberCoroutineScope()
    val repository = ProspectosRepository()
    var isLoading by remember { mutableStateOf(true) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedProspecto by remember { mutableStateOf<ProspectoModel?>(null) }
    var prospectos by remember { mutableStateOf<List<ProspectoModel>>(emptyList()) }
    var lastDocument by remember { mutableStateOf<DocumentSnapshot?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }
    // Definir las animaciones de transformación
    val scale = animateFloatAsState(targetValue = if (showMenu) 0.8f else 1f)
    val offsetX = animateFloatAsState(targetValue = if (showMenu) 600.dp.value else 0f)
    val shadow = animateFloatAsState(targetValue = if (showMenu) 16f else 0f)
    val radius = animateFloatAsState(targetValue = if (showMenu) 20.dp.value else 0f)
    var currentScreen by remember { mutableStateOf("home") }

    LaunchedEffect(showBottomSheet) {
        Log.d("FloatingActionButton", "showBottomSheet is now: $showBottomSheet")
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AnimatedVisibility(visible = showMenu) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Gray)
                    .align(Alignment.CenterStart)
            ) {
                Button(onClick = { currentScreen = "home"; showMenu = false }) { Text("Home") }
                Button(onClick = {
                    currentScreen = "profile"; showMenu = false
                }) { Text("Profile") }
                Button(onClick = {
                    currentScreen = "calendar"; showMenu = false
                }) { Text("Calendar") }
                Button(onClick = {
                    onLogout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }) {
                    Text("Cerrar sesión")
                }

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    translationX = offsetX.value
                    shadowElevation = shadow.value
                }
                .fillMaxSize()
                .clip(RoundedCornerShape(radius.value))
                .background(Color.White),
        ) {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.Menu, contentDescription = "Menú")
            }
//            Text(text = "Bienvenido, administrador", color = Color.Black)
            when (currentScreen) {
                "home" -> HomeScreen(
                    prospectos,
                    isLoading,
                    isLoadingMore,
                    lastDocument,
                    scope,
                    repository
                )

                "calendar" -> CalendarScreen()  // Desarrolla este componente según tus necesidades
                "profile" -> ProfileScreen()  // Desarrolla este componente según tus necesidades
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
fun ProfileScreen() {
    // Perfil del usuario aquí
}




