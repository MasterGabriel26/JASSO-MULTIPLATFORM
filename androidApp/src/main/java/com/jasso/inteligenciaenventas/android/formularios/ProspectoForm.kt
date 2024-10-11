package com.jasso.inteligenciaenventas.android.formularios

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jasso.inteligenciaenventas.android.componentes.DropdownMenuComponent
import com.jasso.inteligenciaenventas.android.componentes.TextFieldComponent
import com.jasso.inteligenciaenventas.models.ProspectoModel
import com.jasso.inteligenciaenventas.models.Telefono
import com.jasso.inteligenciaenventas.repositories.ProspectosRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton


@Composable
fun ProspectoForm(
    uid: String, // Pasamos el UID del usuario actual como argumento
    rol: String,
    prospectoToEdit: ProspectoModel? = null,
    onSubmit: (ProspectoModel) -> Unit,
) {

    // Initialize form fields with values from prospectoToEdit or use defaults
    var nombre by remember { mutableStateOf(prospectoToEdit?.nombre ?: "") }
    var apellidoPaterno by remember { mutableStateOf(prospectoToEdit?.apellido_paterno ?: "") }
    var apellidoMaterno by remember { mutableStateOf(prospectoToEdit?.apellido_materno ?: "") }
    var edad by remember { mutableStateOf(prospectoToEdit?.edad?.toString() ?: "") }
    var genero by remember { mutableStateOf(prospectoToEdit?.genero ?: "") }
    var email by remember { mutableStateOf(prospectoToEdit?.email ?: "") }

    var nombrePareja by remember { mutableStateOf(prospectoToEdit?.nombre_pareja ?: "") }
    var apellidoPaternoPareja by remember { mutableStateOf(prospectoToEdit?.apellido_paterno_pareja ?: "") }
    var apellidoMaternoPareja by remember { mutableStateOf(prospectoToEdit?.apellido_materno_pareja ?: "") }
    var edadPareja by remember { mutableStateOf(prospectoToEdit?.edad_pareja?.toString() ?: "") }
    var generoPareja by remember { mutableStateOf(prospectoToEdit?.genero_pareja ?: "") }

    var invitados by remember { mutableStateOf(prospectoToEdit?.invitados?.toString() ?: "") }
    var descripcion by remember { mutableStateOf(prospectoToEdit?.descripcion ?: "") }
    var pregunto_por by remember { mutableStateOf(prospectoToEdit?.pregunto_por ?: "") }
    var tipoEvento by remember { mutableStateOf(prospectoToEdit?.tipo_evento ?: "") }

    // Default lists for dropdowns
    val opcionesLada = listOf("52", "999", "Otro")
    val opcionesGenero = listOf("Masculino", "Femenino", "Otro")
    val opcionesTipoEvento = listOf("Boda", "XVaño", "Posadas", "Petite", "Graduacion", "Empresarial", "Cumpleaños", "Bautizos y Primeras Comuniones", "Reunión", "Otro")
    val opcionesPreguntoPor = listOf("Casa Antigua", "VCarranza", "Campanario", "Museo de las Aves")

    // Address fields
    var calleUno by remember { mutableStateOf(prospectoToEdit?.calle_uno ?: "") }
    var calleDos by remember { mutableStateOf(prospectoToEdit?.calle_dos ?: "") }
    var numeroInterior by remember { mutableStateOf(prospectoToEdit?.numero_interior?.toString() ?: "") }
    var numeroExterior by remember { mutableStateOf(prospectoToEdit?.numero_exterior?.toString() ?: "") }
    var codigoPostal by remember { mutableStateOf(prospectoToEdit?.codigo_postal?.toString() ?: "") }
    var entreCalleUno by remember { mutableStateOf(prospectoToEdit?.entre_calle_uno ?: "") }
    var entreCalleDos by remember { mutableStateOf(prospectoToEdit?.entre_calle_dos ?: "") }

    // Phone fields
    var ladaProspecto by remember { mutableStateOf(prospectoToEdit?.telefonos?.getOrNull(0)?.lada ?: "") }
    var numeroTelefonoProspecto by remember { mutableStateOf(prospectoToEdit?.telefonos?.getOrNull(0)?.numero ?: "") }
    var ladaPareja by remember { mutableStateOf(prospectoToEdit?.telefonos?.getOrNull(1)?.lada ?: "") }
    var numeroTelefonoPareja by remember { mutableStateOf(prospectoToEdit?.telefonos?.getOrNull(1)?.numero ?: "") }

    // Date fields
    var fechaNacimiento by remember { mutableStateOf(prospectoToEdit?.fecha_nacimiento ?: 0L) }
    var fechaNacimientoPareja by remember { mutableStateOf(prospectoToEdit?.fecha_nacimiento_pareja ?: 0L) }
    var fechaEvento by remember { mutableStateOf(prospectoToEdit?.fecha_evento ?: 0L) }
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val fechaNacimientoFormatted = remember(fechaNacimiento) { simpleDateFormat.format(Date(fechaNacimiento)) }
    val fechaNacimientoParejaFormatted = remember(fechaNacimientoPareja) { simpleDateFormat.format(Date(fechaNacimientoPareja)) }
    val fechaEventoFormatted = remember(fechaEvento) { simpleDateFormat.format(Date(fechaEvento)) }
    // DatePickers setup
    val context = LocalContext.current
    val calendario = Calendar.getInstance()
    val repository = ProspectosRepository()
    val coroutineScope = rememberCoroutineScope()


    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendario.set(year, month, dayOfMonth)
            fechaNacimiento = calendario.timeInMillis
        },
        calendario.get(Calendar.YEAR),
        calendario.get(Calendar.MONTH),
        calendario.get(Calendar.DAY_OF_MONTH)
    )
    val datePickerDialogPareja = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendario.set(year, month, dayOfMonth)
            fechaNacimientoPareja =  calendario.timeInMillis
        },
        calendario.get(Calendar.YEAR),
        calendario.get(Calendar.MONTH),
        calendario.get(Calendar.DAY_OF_MONTH)
    )
    val datePickerDialogEvento = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendario.set(year, month, dayOfMonth)
            fechaEvento =  calendario.timeInMillis
        },
        calendario.get(Calendar.YEAR),
        calendario.get(Calendar.MONTH),
        calendario.get(Calendar.DAY_OF_MONTH)
    )

    var showCoupleFields by remember { mutableStateOf(false) }
    var showCoupleFieldsDireccion by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Agregar Prospecto", modifier = Modifier.padding(bottom = 8.dp))

        // Campos del prospecto
        TextFieldComponent(value = nombre, onValueChange = { nombre = it }, label = "Nombre", modifier = Modifier.fillMaxWidth(), spacerHeight = 8)
        TextFieldComponent(value = apellidoPaterno, onValueChange = { apellidoPaterno = it }, label = "Apellido Paterno", modifier = Modifier.fillMaxWidth(), spacerHeight = 8)
        TextFieldComponent(value = apellidoMaterno, onValueChange = { apellidoMaterno = it }, label = "Apellido Materno", modifier = Modifier.fillMaxWidth(), spacerHeight = 8)
        TextFieldComponent(value = email, onValueChange = { email = it }, label = "Correo", modifier = Modifier.fillMaxWidth(), spacerHeight = 8)

        Row(modifier = Modifier.fillMaxWidth()) {
            TextFieldComponent(value = edad, onValueChange = { edad = it }, label = "Edad", modifier = Modifier.weight(1f).padding(end = 8.dp), keyboardType = KeyboardType.Number, spacerHeight = 16)
            DropdownMenuComponent(label = "Género", opciones = opcionesGenero, selectedOption = genero, onOptionSelected = { genero = it }, spacerHeight = 16, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            DropdownMenuComponent(label = "Lada", opciones = opcionesLada, selectedOption = ladaProspecto, onOptionSelected = { ladaProspecto = it }, spacerHeight = 16, modifier = Modifier.width(140.dp).padding(end = 8.dp))
            TextFieldComponent(value = numeroTelefonoProspecto, onValueChange = { numeroTelefonoProspecto = it }, label = "Teléfono", keyboardType = KeyboardType.Number, spacerHeight = 16)
        }
        OutlinedTextField(
            value = fechaNacimientoFormatted,
            onValueChange = { },
            label = { Text("Fecha de Nacimiento") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Text(text = "\uD83D\uDCC5", modifier = Modifier.padding(8.dp), fontSize = 24.sp)
                }
            }
        )
        Button(
            onClick = { showCoupleFields = !showCoupleFields },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(if (showCoupleFields) "Ocultar Información de la Pareja" else "Agregar Información de la Pareja")
        }
        AnimatedVisibility(
            visible = showCoupleFields,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextFieldComponent(value = nombrePareja, onValueChange = { nombrePareja = it }, label = "Nombre", modifier = Modifier.fillMaxWidth(), spacerHeight = 16)
                TextFieldComponent(value = apellidoPaternoPareja, onValueChange = { apellidoPaternoPareja = it }, label = "Apellido Paterno", modifier = Modifier.fillMaxWidth(), spacerHeight = 16)
                TextFieldComponent(value = apellidoMaternoPareja, onValueChange = { apellidoMaternoPareja = it }, label = "Apellido Materno", modifier = Modifier.fillMaxWidth(), spacerHeight = 16)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextFieldComponent(value = edadPareja, onValueChange = { edadPareja = it }, label = "Edad", modifier = Modifier.width(140.dp), keyboardType = KeyboardType.Number, spacerHeight = 16)
                    DropdownMenuComponent(label = "Género", opciones = opcionesGenero, selectedOption = generoPareja, onOptionSelected = { generoPareja = it }, spacerHeight = 16, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    DropdownMenuComponent(label = "Lada", opciones = opcionesLada, selectedOption = ladaPareja, onOptionSelected = { ladaPareja = it }, spacerHeight = 16, modifier = Modifier.width(140.dp))
                    TextFieldComponent(value = numeroTelefonoPareja, onValueChange = { numeroTelefonoPareja = it }, label = "Teléfono", keyboardType = KeyboardType.Number, spacerHeight = 16)
                }
                OutlinedTextField(
                    value = fechaNacimientoParejaFormatted,
                    onValueChange = { },
                    label = { Text("Fecha de Nacimiento") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialogPareja.show() }) {
                            Text(text = "\uD83D\uDCC5", modifier = Modifier.padding(8.dp), fontSize = 24.sp)
                        }
                    }
                )
            }
        }
        // Campos para la pareja
        Button(
            onClick = { showCoupleFieldsDireccion = !showCoupleFieldsDireccion },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(if (showCoupleFieldsDireccion) "Ocultar Información de la Direccion" else "Agregar Información de la Direccion")
        }
        AnimatedVisibility(
            visible = showCoupleFieldsDireccion,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Dirección
                TextFieldComponent(value = calleUno, onValueChange = { calleUno = it }, label = "Calle 1", modifier = Modifier.fillMaxWidth(), spacerHeight = 16)
                TextFieldComponent(value = calleDos, onValueChange = { calleDos = it }, label = "Calle 2", modifier = Modifier.fillMaxWidth(), spacerHeight = 16)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextFieldComponent(value = numeroInterior, onValueChange = { numeroInterior = it }, label = "Número Interior", modifier = Modifier.weight(1f).padding(end = 8.dp), keyboardType = KeyboardType.Number, spacerHeight = 16)
                    TextFieldComponent(value = numeroExterior, onValueChange = { numeroExterior = it }, label = "Número Exterior", modifier = Modifier.weight(1f), keyboardType = KeyboardType.Number, spacerHeight = 16)
                }
                TextFieldComponent(value = codigoPostal, onValueChange = { codigoPostal = it }, label = "Código Postal", modifier = Modifier.fillMaxWidth(), keyboardType = KeyboardType.Number, spacerHeight = 16)
                TextFieldComponent(value = entreCalleUno, onValueChange = { entreCalleUno = it }, label = "Entre Calle 1", modifier = Modifier.fillMaxWidth(), spacerHeight = 16)
                TextFieldComponent(value = entreCalleDos, onValueChange = { entreCalleDos = it }, label = "Entre Calle 2", modifier = Modifier.fillMaxWidth(), spacerHeight = 16)
            }
        }

        TextFieldComponent(value = invitados, onValueChange = { invitados = it }, label = "Número de Invitados", modifier = Modifier.fillMaxWidth(), keyboardType = KeyboardType.Number, spacerHeight = 16)

        // Campo para "Descripción"
        TextFieldComponent(value = descripcion, onValueChange = { descripcion = it }, label = "Descripción del evento", modifier = Modifier.fillMaxWidth(), spacerHeight = 16)

        // Campo para "Preguntó por"
        DropdownMenuComponent(label = "¿preguntó por?", opciones = opcionesPreguntoPor, selectedOption = pregunto_por, onOptionSelected = { pregunto_por = it }, spacerHeight = 16)

        // Fecha de Nacimiento y Evento
        OutlinedTextField(
            value = fechaEventoFormatted,
            onValueChange = {  },
            label = { Text("Fecha del Evento") },
            readOnly = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            trailingIcon = {
                IconButton(onClick = { datePickerDialogEvento.show() }) {
                    Text(text = "\uD83D\uDCC5", modifier = Modifier.padding(8.dp), fontSize = 24.sp)
                }
            }
        )

        // Tipo de evento
        DropdownMenuComponent(label = "Tipo de Evento", opciones = opcionesTipoEvento, selectedOption = tipoEvento, onOptionSelected = { tipoEvento = it }, spacerHeight = 16, )

        // Botón para guardar el formulario
        Button(
            onClick = {
                coroutineScope.launch {
                try {
                    val telefonoYaExiste = repository.isPhoneNumberExists(numeroTelefonoProspecto)
                    if (telefonoYaExiste) {
                        showPhoneExistsDialog(context, numeroTelefonoProspecto)
                    } else {
                        val updatedProspecto = ProspectoModel(
                            nombre = if (nombre.isNotEmpty()) nombre.lowercase(Locale.getDefault()) else prospectoToEdit?.nombre ?: "",
                            nombre_pareja = if (nombrePareja.isNotEmpty()) nombrePareja.lowercase(Locale.getDefault()) else prospectoToEdit?.nombre_pareja ?: "",
                            apellido_paterno = if (apellidoPaterno.isNotEmpty()) apellidoPaterno.lowercase(Locale.getDefault()) else prospectoToEdit?.apellido_paterno ?: "",
                            apellido_paterno_pareja = if (apellidoPaternoPareja.isNotEmpty()) apellidoPaternoPareja.lowercase(Locale.getDefault()) else prospectoToEdit?.apellido_paterno_pareja ?: "",
                            apellido_materno = if (apellidoMaterno.isNotEmpty()) apellidoMaterno.lowercase(Locale.getDefault()) else prospectoToEdit?.apellido_materno ?: "",
                            apellido_materno_pareja = if (apellidoMaternoPareja.isNotEmpty()) apellidoMaternoPareja.lowercase(Locale.getDefault()) else prospectoToEdit?.apellido_materno_pareja ?: "",
                            edad = if (edad.isNotEmpty()) edad.toInt() else prospectoToEdit?.edad ?: 0,
                            edad_pareja = if (edadPareja.isNotEmpty()) edadPareja.toInt() else prospectoToEdit?.edad_pareja ?: 0,
                            genero = if (genero.isNotEmpty()) genero.lowercase(Locale.getDefault()) else prospectoToEdit?.genero ?: "",
                            genero_pareja = if (generoPareja.isNotEmpty()) generoPareja.lowercase(Locale.getDefault()) else prospectoToEdit?.genero_pareja ?: "",
                            invitados = if (invitados.isNotEmpty()) invitados.toInt() else prospectoToEdit?.invitados ?: 0,
                            descripcion = if (descripcion.isNotEmpty()) descripcion.lowercase(Locale.getDefault()) else prospectoToEdit?.descripcion ?: "",
                            pregunto_por = if (pregunto_por.isNotEmpty()) pregunto_por.lowercase(Locale.getDefault()) else prospectoToEdit?.pregunto_por ?: "",
                            tipo_evento = if (tipoEvento.isNotEmpty()) tipoEvento.lowercase(Locale.getDefault()) else prospectoToEdit?.tipo_evento ?: "",
                            calle_uno = if (calleUno.isNotEmpty()) calleUno.lowercase(Locale.getDefault()) else prospectoToEdit?.calle_uno ?: "",
                            calle_dos = if (calleDos.isNotEmpty()) calleDos.lowercase(Locale.getDefault()) else prospectoToEdit?.calle_dos ?: "",
                            numero_interior = if (numeroInterior.isNotEmpty()) numeroInterior.toInt() else prospectoToEdit?.numero_interior ?: 0,
                            numero_exterior = if (numeroExterior.isNotEmpty()) numeroExterior.toInt() else prospectoToEdit?.numero_exterior ?: 0,
                            codigo_postal = if (codigoPostal.isNotEmpty()) codigoPostal.toInt() else prospectoToEdit?.codigo_postal ?: 0,
                            entre_calle_uno = if (entreCalleUno.isNotEmpty()) entreCalleUno.lowercase(Locale.getDefault()) else prospectoToEdit?.entre_calle_uno ?: "",
                            entre_calle_dos = if (entreCalleDos.isNotEmpty()) entreCalleDos.lowercase(Locale.getDefault()) else prospectoToEdit?.entre_calle_dos ?: "",
                            telefonos = listOf(
                                Telefono(
                                    lada = if (ladaProspecto.isNotEmpty()) ladaProspecto else prospectoToEdit?.telefonos?.getOrNull(0)?.lada ?: "",
                                    numero = if (numeroTelefonoProspecto.isNotEmpty()) numeroTelefonoProspecto else prospectoToEdit?.telefonos?.getOrNull(0)?.numero ?: ""
                                ),
                                Telefono(
                                    lada = if (ladaPareja.isNotEmpty()) ladaPareja else prospectoToEdit?.telefonos?.getOrNull(1)?.lada ?: "",
                                    numero = if (numeroTelefonoPareja.isNotEmpty()) numeroTelefonoPareja else prospectoToEdit?.telefonos?.getOrNull(1)?.numero ?: ""
                                )
                            ),
                            fecha_nacimiento = fechaNacimiento ?: 0L,
                            fecha_nacimiento_pareja = fechaNacimientoPareja ?: 0L,
                            fecha_evento = fechaEvento ?: 0L,
                            uid = prospectoToEdit?.uid,
                            rol = "prospecto",
                            asesor_creador = uid,
                            status = "interesado"
                        )
                        onSubmit(updatedProspecto)
                    }



                } catch (e: Exception) {
                    Log.e("ProspectoForm", "Error al crear prospecto: ${e.message}", e)
                }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Prospecto")
        }

    }
}
fun showPhoneExistsDialog(context: Context, phoneNumber: String) {
    AlertDialog.Builder(context)
        .setTitle("Teléfono ya registrado")
        .setMessage("El número de teléfono $phoneNumber ya está registrado en los prospectos.")
        .setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss() // Cierra el diálogo al hacer clic en "Aceptar"
        }
        .show()
}

