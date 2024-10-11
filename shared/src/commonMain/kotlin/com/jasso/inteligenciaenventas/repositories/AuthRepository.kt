package com.jasso.inteligenciaenventas.repositories

import com.jasso.inteligenciaenventas.models.UserModel

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<UserModel>
    suspend fun signIn(email: String, password: String): Result<UserModel>
    suspend fun getCurrentUser(): UserModel?
}
