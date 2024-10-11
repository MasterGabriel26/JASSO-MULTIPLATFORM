import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jasso.inteligenciaenventas.models.ProspectoModel

@Composable
fun ProspectoItem(user: ProspectoModel, onEdit: (ProspectoModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp), // Correct way to set elevation in Material3
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Espaciado interno del card
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Nombre: ${user.nombre ?: "Desconocido"}", color = Color.Black)
                Spacer(modifier = Modifier.weight(1f)) // Para empujar el botón hacia la derecha
                Button(onClick = { onEdit(user) }) {
                    Text("Editar")
                }
            }
            Text(text = "Apellido: ${user.apellido_paterno ?: "Desconocido"}", color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
