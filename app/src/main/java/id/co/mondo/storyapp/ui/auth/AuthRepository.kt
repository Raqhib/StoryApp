package id.co.mondo.storyapp.ui.auth

import id.co.mondo.storyapp.network.response.LoginResponse
import id.co.mondo.storyapp.network.response.RegisterResponse
import id.co.mondo.storyapp.network.retrofit.ApiService

class AuthRepository(private val apiService: ApiService) {

    // Fungsi untuk melakukan register
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        val response = apiService.register(name, email, password)
        return if (response.error == true) {
            RegisterResponse(error = true, message = response.message)
        } else {
            response
        }
    }

    // Fungsi untuk melakukan login
    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        return if (response.error == true) {
            LoginResponse(error = true, message = response.message, loginResult = response.loginResult)
        } else {
            response
        }
    }

}