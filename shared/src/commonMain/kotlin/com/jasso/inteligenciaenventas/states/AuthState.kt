package com.jasso.inteligenciaenventas.states

import com.jasso.inteligenciaenventas.models.UserModel

sealed class AuthState {
    data object Empty : AuthState()
    data class Success(val user: UserModel) : AuthState()
    data class Error(val message: String) : AuthState()
}
