package com.jasso.inteligenciaenventas.android.Homes.Screens

import ProspectoItem
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.DocumentSnapshot
import com.jasso.inteligenciaenventas.android.formularios.ProspectoForm
import com.jasso.inteligenciaenventas.models.ProspectoModel
import com.jasso.inteligenciaenventas.repositories.ProspectosRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    prospectos: List<ProspectoModel>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    lastDocument: DocumentSnapshot?,
    scope: CoroutineScope,
    repository: ProspectosRepository
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedProspecto by remember { mutableStateOf<ProspectoModel?>(null) }
    var isLoadingMore by remember { mutableStateOf(false) }
    val pageSize = 20L
    var prospectos by remember { mutableStateOf<List<ProspectoModel>>(emptyList()) }
    var lastDocument by remember { mutableStateOf<DocumentSnapshot?>(null) }
    var uid by remember { mutableStateOf<String?>(null) }
    var role by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            uid = repository.getCurrentUserId()  // Obtener el UID
            uid?.let {
                role = repository.getCurrentUserRole(it)  // Si el UID no es null, obtener el rol
            }
            val (newProspectos, lastDoc) = repository.getProspectos(limit = pageSize)
            prospectos = newProspectos
            lastDocument = lastDoc
            isLoading = false
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(prospectos) { prospecto ->
                    ProspectoItem(prospecto) { selected ->
                        showBottomSheet = true
                        selectedProspecto = selected
                    }
                }

                // Detecta si el usuario llegó al final de la lista y carga más prospectos
                item {
                    if (!isLoadingMore && lastDocument != null) {
                        Button(onClick = {
                            scope.launch {
                                isLoadingMore = true
                                val (newProspectos, lastDoc) = repository.getProspectos(
                                    lastDocument,
                                    limit = pageSize
                                )
                                prospectos =
                                    prospectos + newProspectos  // Agregar nuevos prospectos a la lista existente
                                lastDocument = lastDoc
                                isLoadingMore = false
                            }
                        }) {
                            Text("Cargar más")
                        }
                    }

                    if (isLoadingMore) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                selectedProspecto = null
                showBottomSheet = true
            },
            modifier = Modifier
            .align(Alignment.BottomEnd)  // Aquí ya no tendrás el error
                .padding(16.dp),
            containerColor = Color.Black
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Agregar Prospecto",
                tint = Color.White
            )
        }
    }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = Color.White,
            scrimColor = Color.Black.copy(alpha = 0.7f),
            modifier = Modifier
                .fillMaxSize()
        ) {
            // El formulario se envuelve en una LazyColumn para agregar scroll
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(min = 500.dp)

            ) {
                item {
                    ProspectoForm(
                        uid = uid ?: "Unknown UID",
                        rol = role ?: "Unknown Role",
                        prospectoToEdit = selectedProspecto,
                    ) { prospecto ->
                        scope.launch {
                            if (selectedProspecto == null) {
                                // Crear nuevo prospecto
                                repository.addProspecto(prospecto)
                            } else {
                                // Actualizar prospecto existente
                                repository.updateProspecto(prospecto)
                            }
                            val (newProspectos, lastDoc) = repository.getProspectos(limit = pageSize)
                            prospectos = newProspectos
                            lastDocument = lastDoc
                        }
                        showBottomSheet = false
                    }
                }
            }
        }
    }

}
