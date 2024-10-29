package com.jasso.inteligenciaenventas.android.Homes.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.jasso.inteligenciaenventas.android.formularios.AgregarCitaForm
import com.jasso.inteligenciaenventas.models.CalendarioModel
import com.jasso.inteligenciaenventas.repositories.EventosRepository
import com.jasso.inteligenciaenventas.repositories.ProspectosRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val selectedDate = remember { mutableStateOf<Long?>(null) } // Manteniendo la fecha seleccionada como Long
    val isCalendarVisible = remember { mutableStateOf(true) }
    val mexicoZoneId = ZoneId.of("America/Mexico_City")
    val events = remember { mutableStateListOf<CalendarioModel>() } // Aquí almacenamos los eventos cargados desde Firestore
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(currentMonth.value) {
        val startMonth = currentMonth.value.atDay(1).atStartOfDay(mexicoZoneId).toInstant().toEpochMilli()
        val endMonth = currentMonth.value.atEndOfMonth().atTime(23, 59).atZone(mexicoZoneId).toInstant().toEpochMilli()

        Log.d("CalendarDebug", "Consultando eventos de ${startMonth} a ${endMonth}")

        db.collection("CITAS")
            .whereGreaterThanOrEqualTo("fechaAgenda", startMonth)
            .whereLessThan("fechaAgenda", endMonth)
            .get()
            .addOnSuccessListener { citasSnapshot ->
                events.clear()
                citasSnapshot.documents.forEach { citaDocument ->
                    citaDocument.toObject(CalendarioModel::class.java)?.let { evento ->
                        events.add(evento)
                        Log.d("CalendarDebug", "Evento añadido: $evento")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("CalendarDebug", "Error al cargar citas: ${e.message}", e)
            }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("${currentMonth.value.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.value.year}")
                },
                actions = {
                    IconButton(onClick = { currentMonth.value = currentMonth.value.minusMonths(1) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Month")
                    }
                    IconButton(onClick = { currentMonth.value = currentMonth.value.plusMonths(1) }) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Month")
                    }
                    // Botón para ocultar/mostrar el calendario
                    TextButton(onClick = { isCalendarVisible.value = !isCalendarVisible.value }) {
                        Text(if (isCalendarVisible.value) "Ocultar" else "Mostrar", color = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxWidth().background(Color.Black)) {
            if (isCalendarVisible.value) {
                WeekDaysHeader()
                MonthView(currentMonth.value, selectedDate.value, mexicoZoneId, events) { date ->
                    selectedDate.value = date.atStartOfDay(mexicoZoneId).toInstant().toEpochMilli()
                }
                MonthRow(currentMonth.value) { selectedMonth ->
                    currentMonth.value = selectedMonth
                }
            }
            EventList(selectedDate.value, events, onEventAdded = { newEvent ->
                // Actualizar la lista de eventos en tiempo real
                events.add(newEvent)
            })
        }
    }
}

@Composable
fun WeekDaysHeader() {
    val weekFields = WeekFields.of(DayOfWeek.MONDAY, 1)
    val daysOfWeek = remember { listOf(7, 1, 2, 3, 4, 5, 6) }
    Row(modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(text = weekFields.firstDayOfWeek.plus(dayOfWeek.toLong() - 1).getDisplayName(TextStyle.SHORT, Locale.getDefault()), modifier = Modifier.weight(1f).padding(horizontal = 4.dp), color = Color.White, textAlign = TextAlign.Center)
        }
    }
}


@Composable
fun MonthView(month: YearMonth, selectedDate: Long?, zoneId: ZoneId, events: List<CalendarioModel>, onDateSelected: (LocalDate) -> Unit) {
    val startDayOfMonth = month.atDay(1)
    val endDayOfMonth = month.atEndOfMonth()
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    val startOfCalendar = startDayOfMonth.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
    val endOfCalendar = endDayOfMonth.with(TemporalAdjusters.nextOrSame(firstDayOfWeek))
    val days = generateSequence(startOfCalendar) { it.plusDays(1) }.takeWhile { !it.isAfter(endOfCalendar) }.toList()

    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.padding(4.dp).fillMaxWidth(), contentPadding = PaddingValues(4.dp)) {
        items(days) { date ->
            val isCurrentMonth = date.month == month.month
            val dateInMillis = date.atStartOfDay(zoneId).toInstant().toEpochMilli()
            val isSelected = dateInMillis == selectedDate
            val hasEvents = checkForEventsOnDate(date, events) // Aquí pasamos los eventos

            DayCell(date, isSelected = isSelected, isCurrentMonth = isCurrentMonth, hasEvents = hasEvents) {
                onDateSelected(date)
            }
        }
    }
}

@Composable
fun MonthRow(currentMonth: YearMonth, onMonthSelected: (YearMonth) -> Unit) {
    // Genera una lista de meses desde diciembre de 2023 hasta enero de 2030
    val months = remember {
        val startMonth = YearMonth.of(2023, 12) // Empezar en diciembre de 2023
        val endMonth = YearMonth.of(2030, 1) // Terminar en enero de 2030
        generateSequence(startMonth) { month ->
            if (month == endMonth) null else month.plusMonths(1)
        }.toList()
    }

    LazyRow(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.Start) {
        items(months) { month ->
            // Mostrar el año solo cuando cambia en diciembre
            val displayText = when (month.month) {
                java.time.Month.DECEMBER -> "dic, ${month.year}"
                java.time.Month.JANUARY -> "ene"
                else -> month.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            }

            TextButton(onClick = { onMonthSelected(month) }) {
                Text(
                    text = displayText,
                    color = if (month == currentMonth) Color.Yellow else Color.White
                )
            }
        }
    }
}



fun checkForEventsOnDate(date: LocalDate, events: List<CalendarioModel>): Boolean {
    val zoneId = ZoneId.of("America/Mexico_City")
    val startOfDayMillis = date.atStartOfDay(zoneId).toInstant().toEpochMilli()
    val endOfDayMillis = date.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()

    return events.any { event ->
        event.fechaAgenda != null && event.fechaAgenda in startOfDayMillis until endOfDayMillis
    }
}

@Composable
fun DayCell(date: LocalDate, isSelected: Boolean, isCurrentMonth: Boolean, hasEvents: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(2.dp).aspectRatio(1f).fillMaxWidth().clickable(enabled = isCurrentMonth, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primary else if (!isCurrentMonth) Color.LightGray else Color.DarkGray)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = date.dayOfMonth.toString(), style = MaterialTheme.typography.bodyLarge.copy(color = if (isCurrentMonth) Color.White else Color.Gray))
            if (hasEvents) {
                Box(modifier = Modifier.align(Alignment.BottomEnd).size(8.dp).background(Color.Red, shape = CircleShape))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventList(selectedDate: Long?, events: List<CalendarioModel>, onEventAdded: (CalendarioModel) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var uid by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val repository = ProspectosRepository()
    LaunchedEffect(Unit) {
        scope.launch {
            uid = repository.getCurrentUserId()
        }
//        events.add(CalendarioModel("Evento de Prueba", "Nombre del Cliente", "reunión", LocalDate.now().atStartOfDay(ZoneId.of("America/Mexico_City")).toInstant().toEpochMilli(), LocalDate.now().atStartOfDay(ZoneId.of("America/Mexico_City")).toInstant().toEpochMilli(), "08:00", "Oficina", "Descripción"))
    }
    val dayEvents = remember(selectedDate) {
        events.filter { event ->
            selectedDate != null && event.fechaAgenda != null &&
                    event.fechaAgenda!! >= selectedDate && event.fechaAgenda!! < selectedDate + 86400000 // Comparar dentro del rango del día
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (dayEvents.isEmpty()) {
            Text("No hay eventos para este día", color = Color.White, modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                items(dayEvents) { event ->
                    EventItem(event)
                }
            }
        }
        FloatingActionButton(
            onClick = { showBottomSheet = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = Color.Black
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar Prospecto", tint = Color.White)
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = Color.White,
                scrimColor = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize().heightIn(min = 500.dp)) {
                    item {
                        AgregarCitaForm(uid!!, onCloseBottomSheet = { showBottomSheet = false }, onEventAdded = { newEvent ->
                            onEventAdded(newEvent)
                        })
                    }
                }
            }
        }
    }
}
@Composable
fun EventItem(event: CalendarioModel) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = event.descripcion!!, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.horaInicio!!, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.nombreCliente!!, style = MaterialTheme.typography.bodySmall)
        }
    }
}
