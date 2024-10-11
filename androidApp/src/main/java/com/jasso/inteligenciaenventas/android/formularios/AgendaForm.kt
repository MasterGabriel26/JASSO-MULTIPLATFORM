package com.jasso.inteligenciaenventas.android.formularios

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.jasso.inteligenciaenventas.android.componentes.DropdownMenuComponent
import com.jasso.inteligenciaenventas.android.componentes.TextFieldComponent
import com.jasso.inteligenciaenventas.models.CalendarioModel
import com.jasso.inteligenciaenventas.models.ProspectoModel
import com.jasso.inteligenciaenventas.repositories.ProspectosRepository
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarCitaForm(
    usuarioId: String,
    onCloseBottomSheet: () -> Unit,
    onEventAdded: (CalendarioModel) -> Unit
) {
    val context = LocalContext.current
    val prospectosRepository = ProspectosRepository()

    // Variables para los campos del formulario
    var asunto by remember { mutableStateOf("") }
    var nombreCliente by remember { mutableStateOf("") }
    var tipoAgenda by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var fechaAgenda by remember { mutableStateOf(0L) }
    var horaInicio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var prospectosList by remember { mutableStateOf<List<ProspectoModel>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }

    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val fechaAgendaFormatted = remember(fechaAgenda) {
        if (fechaAgenda != 0L) {
            simpleDateFormat.format(Date(fechaAgenda))
        } else {
            "Selecciona una fecha"
        }
    }

    // Opciones predefinidas para Tipo de Agenda y Lugar
    val tipoAgendaOptions = listOf("Reunión", "Consulta", "Evento", "Tarea")
    val lugarOptions = listOf("Sala de reuniones", "Oficina", "Remoto", "Otro")

    // Función para buscar prospectos mientras se escribe
    LaunchedEffect(nombreCliente) {
        if (nombreCliente.isNotEmpty()) {
            val result = prospectosRepository.searchProspectosByName(nombreCliente.lowercase())
            prospectosList = result
            showSuggestions = result.isNotEmpty()//registro clientes
        } else {
            showSuggestions = false
        }
    }

    // DatePickerDialog para la fecha de la agenda
    val calendario = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendario.set(year, month, dayOfMonth)
            fechaAgenda = calendario.timeInMillis
        },
        calendario.get(Calendar.YEAR),
        calendario.get(Calendar.MONTH),
        calendario.get(Calendar.DAY_OF_MONTH)
    )

    // TimePickerDialog para la hora de inicio
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            // Formatear la hora seleccionada
            horaInicio = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendario.get(Calendar.HOUR_OF_DAY),
        calendario.get(Calendar.MINUTE),
        true // Elige si mostrar el formato de 24 horas
    )

    Column(modifier = Modifier.padding(16.dp)) {

        // Usamos el TextFieldComponent para Asunto
        TextFieldComponent(
            value = asunto,
            onValueChange = { asunto = it },
            label = "Asunto",
            spacerHeight = 16
        )

        // Campo de texto para Nombre del Cliente con sugerencias
        Box(modifier = Modifier.fillMaxWidth()) {
            TextFieldComponent(
                value = nombreCliente,
                onValueChange = { nombreCliente = it },
                label = "Nombre del cliente",
                spacerHeight = 16
            )

            // Mostrar las sugerencias
            if (showSuggestions) {
                DropdownMenu(
                    expanded = showSuggestions,
                    onDismissRequest = { showSuggestions = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    prospectosList.forEach { prospecto ->
                        DropdownMenuItem(
                            text = { Text(prospecto.nombre!!) },
                            onClick = {
                                nombreCliente = prospecto.nombre!!
                                showSuggestions = false
                            }
                        )
                    }
                }
            }
        }

        // Usamos el componente DropdownMenuComponent para Tipo de Agenda
        DropdownMenuComponent(
            label = "Tipo de agenda",
            opciones = tipoAgendaOptions,
            selectedOption = tipoAgenda,
            onOptionSelected = { tipoAgenda = it },
            spacerHeight = 16
        )

        // Usamos el componente DropdownMenuComponent para Lugar
        DropdownMenuComponent(
            label = "Lugar",
            opciones = lugarOptions,
            selectedOption = lugar,
            onOptionSelected = { lugar = it },
            spacerHeight = 16
        )

        // Fecha de la Agenda
        OutlinedTextField(
            value = fechaAgendaFormatted,
            onValueChange = { },
            label = { Text("Fecha de la Agenda") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
                }
            },
            textStyle = TextStyle(fontSize = 16.sp)
        )

        // Selector de hora para hora de inicio
        OutlinedTextField(
            value = horaInicio,
            onValueChange = { },
            label = { Text("Hora de inicio") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            trailingIcon = {
                IconButton(onClick = { timePickerDialog.show() }) {
                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Seleccionar hora de inicio")
                }
            },
            textStyle = TextStyle(fontSize = 16.sp)
        )

        // Usamos el TextFieldComponent para Descripción
        TextFieldComponent(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = "Descripción",
            spacerHeight = 16
        )

        // Botón para guardar la cita
        Button(onClick = {
            // Generar la fecha de creación automáticamente
            val fechaCreacion = System.currentTimeMillis()

            val nuevaCita = CalendarioModel(
                asunto = asunto,
                nombreCliente = nombreCliente,
                tipoAgenda = tipoAgenda,
                fechaAgenda = fechaAgenda,
                fechaCreacion = fechaCreacion,
                horaInicio = horaInicio,
                lugar = lugar,
                descripcion = descripcion,
                userId = usuarioId // El ID del usuario que crea la cita
            )

            // Guardar la cita directamente en la colección CITAS
            val db = FirebaseFirestore.getInstance()
            db.collection("CITAS")
                .add(nuevaCita)
                .addOnSuccessListener {
                    onEventAdded(nuevaCita) // Añadimos el evento a la lista de eventos
                    onCloseBottomSheet()    // Cerramos el BottomSheet
                }
        }) {
            Text("Guardar Cita")
        }
    }
}
