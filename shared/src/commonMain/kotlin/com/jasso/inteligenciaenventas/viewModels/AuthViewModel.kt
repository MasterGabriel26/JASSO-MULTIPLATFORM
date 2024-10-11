package com.jasso.inteligenciaenventas.viewModels

import com.google.firebase.auth.FirebaseAuth
import com.jasso.inteligenciaenventas.repositories.AuthRepository
import com.jasso.inteligenciaenventas.states.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(private val authRepository: AuthRepository) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Empty)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        GlobalScope.launch {
            val user = authRepository.getCurrentUser()
            _authState.value = if (user != null) AuthState.Success(user) else AuthState.Empty
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        GlobalScope.launch {
            if (password != confirmPassword) {
                _authState.value = AuthState.Error("Passwords do not match")
                return@launch
            }
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = { user -> _authState.value = AuthState.Success(user) },
                onFailure = { exception -> _authState.value = AuthState.Error(exception.message ?: "Unknown error") }
            )
        }
    }

    fun signIn(email: String, password: String) {
        GlobalScope.launch {
            val result = authRepository.signIn(email, password)
            result.fold(
                onSuccess = { user -> _authState.value = AuthState.Success(user) },
                onFailure = { exception -> _authState.value = AuthState.Error(exception.message ?: "Unknown error") }
            )
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _authState.value = AuthState.Empty // Cambiar el estado a no autenticado
    }
}
