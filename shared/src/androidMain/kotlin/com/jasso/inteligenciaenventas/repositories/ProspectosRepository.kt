package com.jasso.inteligenciaenventas.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.jasso.inteligenciaenventas.models.ProspectoModel
import kotlinx.coroutines.tasks.await

class ProspectosRepository {

    private val firestore = Firebase.firestore

    suspend fun getProspectos(lastDocument: DocumentSnapshot? = null, limit: Long = 20): Pair<List<ProspectoModel>, DocumentSnapshot?> {
        val query = if (lastDocument == null) {
            firestore.collection("prospectos").limit(limit)
        } else {
            firestore.collection("prospectos").startAfter(lastDocument).limit(limit)
        }

        val result = query.get().await()
        val prospectos = result.toObjects(ProspectoModel::class.java)
        val lastVisible = result.documents.lastOrNull()

        return Pair(prospectos, lastVisible)
    }


    suspend fun addProspecto(prospecto: ProspectoModel) {
        Log.d("ProspectosRepository", "Agregando prospecto: $prospecto")
        try {
            // Genera un documento vacío en la colección 'prospectos'
            val nuevoDocumento = firestore.collection("prospectos").document()

            // Crea una copia del prospecto con el ID generado como UID
            val prospectoConUID = prospecto.copy(uid = nuevoDocumento.id)

            // Guarda el prospecto en Firestore con el UID actualizado
            nuevoDocumento.set(prospectoConUID).await()

            Log.d("ProspectosRepository", "Prospecto agregado con ID: ${nuevoDocumento.id}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ProspectosRepository", "Error al agregar prospecto: ${e.message}")
        }
    }

    fun getCurrentUserId(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }

    suspend fun getCurrentUserRole(uid: String): String? {
        val firestore = FirebaseFirestore.getInstance()
        return try {
            val userSnapshot = firestore.collection("users").document(uid).get().await()
            userSnapshot.getString("rol") // assuming "rol" is the field name in the Firestore document
        } catch (e: Exception) {
            Log.e("HomeActivity", "Error fetching user role", e)
            null
        }
    }


    suspend fun updateProspecto(prospecto: ProspectoModel) {
        try {
            val documentRef = firestore.collection("prospectos").document(prospecto.uid!!)
            documentRef.set(prospecto).await() // Sobreescribe los datos del prospecto
            Log.d("ProspectosRepository", "Prospecto actualizado con ID: ${prospecto.uid}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ProspectosRepository", "Error al actualizar prospecto: ${e.message}")
        }
    }
    suspend fun isPhoneNumberExists(phoneNumber: String): Boolean {
        return try {
            val query = FirebaseFirestore.getInstance()
                .collection("prospectos")
                .whereEqualTo("telefonos.0.numero", phoneNumber)
                .get()
                .await()

            !query.isEmpty // Si la consulta devuelve algún documento, significa que el teléfono ya existe
        } catch (e: Exception) {
            Log.e("ProspectosRepository", "Error al verificar si el teléfono ya existe: ${e.message}", e)
            false
        }
    }

    suspend fun searchProspectosByName(name: String): List<ProspectoModel> {
        return try {
            val querySnapshot = FirebaseFirestore.getInstance()
                .collection("prospectos")
                .whereGreaterThanOrEqualTo("nombre", name)
                .whereLessThanOrEqualTo("nombre", name + "\uf8ff") // Para la búsqueda de prefijo
                .get()
                .await()

            querySnapshot.toObjects(ProspectoModel::class.java)
        } catch (e: Exception) {
            Log.e("ProspectosRepository", "Error buscando prospectos: ${e.message}")
            emptyList()
        }
    }



}
