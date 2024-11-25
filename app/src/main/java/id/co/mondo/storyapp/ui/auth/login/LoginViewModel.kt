package id.co.mondo.storyapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mondo.storyapp.network.response.LoginResponse
import id.co.mondo.storyapp.network.response.User
import id.co.mondo.storyapp.ui.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _loginState = MutableStateFlow<LoginResponse?>(null)
    val loginState: StateFlow<LoginResponse?> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                _loginState.value = response
            } catch (e: Exception) {
                _loginState.value = LoginResponse(
                    error = true,
                    message = e.message ?: "Terjadi Kesalahan",
                    loginResult = User("", "", "")
                )
            }
        }
    }

}