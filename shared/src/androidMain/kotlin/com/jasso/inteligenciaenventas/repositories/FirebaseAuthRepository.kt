package com.jasso.inteligenciaenventas.repositories

import com.jasso.inteligenciaenventas.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirebaseAuthRepository : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun signUp(email: String, password: String): Result<UserModel> = try {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let { user ->
            val userModel = UserModel(
                fecha_registro = System.currentTimeMillis(),
                uid = user.uid,
                email = user.email ?: "",
                img_profile = null,
                edad = null,
                fecha_nacimiento = null,
                genero = null,
                nombre = null,
                apellido_uno = null,
                apellido_dos = null,
                rol = "usuario",
                status = "activo",
                direccion = null,
                telefonos = emptyList()
            )
            // Guardar el usuario en Firestore
            saveUserToFirestore(userModel)
            Result.success(userModel)
        } ?: Result.failure(Exception("Failed to register user"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signIn(email: String, password: String): Result<UserModel> = try {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        result.user?.let { user ->
            Result.success(
                UserModel(
                    fecha_registro = null,
                    uid = user.uid,
                    email = user.email ?: "",
                    img_profile = null,
                    edad = null,
                    fecha_nacimiento = null,
                    genero = null,
                    nombre = null,
                    apellido_uno = null,
                    apellido_dos = null,
                    rol = null,
                    status = null,
                    direccion = null,
                    telefonos = null
                )
            )
        } ?: Result.failure(Exception("Failed to sign in user"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun saveUserToFirestore(userModel: UserModel) {
        val usersCollection = firestore.collection("users")
        usersCollection.document(userModel.uid.toString()).set(userModel).await()
    }
    override suspend fun getCurrentUser(): UserModel? {
        return firebaseAuth.currentUser?.let { user ->
            UserModel(
                fecha_registro = null,
                uid = user.uid,
                email = user.email ?: "",
                img_profile = null,
                edad = null,
                fecha_nacimiento = null,
                genero = null,
                nombre = null,
                apellido_uno = null,
                apellido_dos = null,
                rol = null,
                status = null,
                direccion = null,
                telefonos = null
            )
        }
    }

}
