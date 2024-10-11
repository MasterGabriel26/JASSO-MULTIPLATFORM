package com.jasso.inteligenciaenventas.android.componentes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    // Estilos predeterminados para el campo de texto
    isError: Boolean = false,
    placeholder: String = "",
    enabled: Boolean = true,
    maxLines: Int = 1, // Valor por defecto para una sola línea
    singleLine: Boolean = true, // Por defecto es un solo renglón
    keyboardType: KeyboardType = KeyboardType.Text, // Por defecto es texto
    imeAction: ImeAction = ImeAction.Done, // Acción del teclado predeterminada
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 16.sp), // Tamaño de fuente por defecto
    visualTransformation: VisualTransformation = VisualTransformation.None,
    labelColor: Color = Color.Gray, // Color de etiqueta predeterminado
    textColor: Color = Color.Black, // Color del texto predeterminado
    backgroundColor: Color = Color.Transparent,
    fontSize: TextUnit = 16.sp, // Tamaño de fuente predeterminado
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    spacerHeight: Int? = null
) {
    // Campo de texto con los valores por defecto y opciones personalizables
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = labelColor) },
        isError = isError,
        placeholder = { Text(placeholder) },
        modifier = modifier,
        enabled = enabled,
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        textStyle = textStyle.copy(color = textColor, fontSize = fontSize),
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
    Spacer(modifier = Modifier.height(spacerHeight!!.dp))
}
