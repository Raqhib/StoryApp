package id.co.mondo.storyapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import id.co.mondo.storyapp.databinding.ActivityLoginBinding
import id.co.mondo.storyapp.network.retrofit.ApiConfig
import id.co.mondo.storyapp.ui.MainActivity
import id.co.mondo.storyapp.ui.auth.AuthRepository
import id.co.mondo.storyapp.ui.auth.register.RegisterActivity
import id.co.mondo.storyapp.ui.utils.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var userPreferences: UserPreferences

    private val loginViewModel: LoginViewModel by viewModels{
        LoginViewModelFactory(AuthRepository(ApiConfig.getApiService()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences.getInstance(this)

        lifecycleScope.launch {
            // Cek token yang tersimpan
            val token = userPreferences.token.firstOrNull()
            Log.d("LoginActivity", "Token ditemukan: $token")
            if (!token.isNullOrEmpty()) {
                // Jika token ada, langsung ke MainActivity
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(email, password)
            }
        }
        observeLoginState()


    }

    private fun observeLoginState() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginState.collect { response ->
                response?.let {
                    if (!it.error!!) {
                        Toast.makeText(this@LoginActivity, "Login berhasil: ${it.loginResult.name}", Toast.LENGTH_SHORT).show()

                        val token = it.loginResult.token
                        Log.d("LoginActivity", "Token dari API: $token")
                        // Simpan token dan username ke UserPreferences
                        if (!token.isNullOrEmpty()) {
                            userPreferences.saveToken(token)
                            userPreferences.saveUserName(it.loginResult.name)
                            Log.d("LoginActivity", "Token berhasil disimpan: $token")
                        } else {
                            Log.e("LoginActivity", "Token kosong atau null.")
                        }

                        // Navigasi ke MainActivity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}