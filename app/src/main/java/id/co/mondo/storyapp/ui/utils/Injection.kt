package id.co.mondo.storyapp.ui.utils

import android.content.Context
import android.util.Log
import id.co.mondo.storyapp.network.retrofit.ApiConfig
import id.co.mondo.storyapp.ui.story.StoryRepository


object Injection {
    suspend fun provideRepository(context: Context): StoryRepository {
        // Mengambil instance UserPreferences
        val pref = UserPreferences.getInstance(context)

        // Mendapatkan token pengguna secara asinkron
        val user = pref.getUser() // Fungsi ini akan berjalan secara suspended
        Log.d("Injection", "Token ditemukan: ${user.token}")

        // Menggunakan token untuk konfigurasi ApiService
        val apiService = ApiConfig.getApiService(user.token)
        Log.d("Injection", "ApiService berhasil dibuat dengan token")

        // Mengembalikan instance StoryRepository
        return StoryRepository.getInstance(apiService, pref)
    }
}
