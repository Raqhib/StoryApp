package id.co.mondo.storyapp.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mondo.storyapp.network.response.RegisterResponse
import id.co.mondo.storyapp.ui.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _registerState = MutableStateFlow<RegisterResponse?>(null)
    val registerState: StateFlow<RegisterResponse?> = _registerState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.register(name, email, password)
                _registerState.value = response
            } catch (e: Exception) {
                _registerState.value = RegisterResponse(error = true, message = e.message)
            }
        }
    }
}