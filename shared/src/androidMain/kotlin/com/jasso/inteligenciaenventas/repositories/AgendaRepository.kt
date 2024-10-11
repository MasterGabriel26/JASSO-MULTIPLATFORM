package com.jasso.inteligenciaenventas.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jasso.inteligenciaenventas.models.CalendarioModel
import kotlinx.coroutines.tasks.await

class EventosRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getEventosPorMes(startMillis: Long, endMillis: Long): List<CalendarioModel> {
        return try {
            val snapshot = firestore.collection("AGENDA")
                .whereGreaterThanOrEqualTo("fechaAgenda", startMillis)
                .whereLessThan("fechaAgenda", endMillis)
                .get()
                .await()

            if (snapshot.isEmpty) {
                Log.d("EventosRepository", "No se encontraron eventos en el rango.")
                emptyList()
            } else {
                snapshot.toObjects(CalendarioModel::class.java).also {
                    Log.d("EventosRepository", "Eventos cargados: ${it.size}")
                }
            }
        } catch (e: Exception) {
            Log.e("EventosRepository", "Error al cargar eventos: ${e.message}", e)
            emptyList()
        }
    }
}
