package id.co.mondo.storyapp.ui.auth.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import id.co.mondo.storyapp.databinding.ActivityRegisterBinding
import id.co.mondo.storyapp.network.retrofit.ApiConfig
import id.co.mondo.storyapp.ui.auth.AuthRepository
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels{
        RegisterViewModelFactory(AuthRepository(ApiConfig.getApiService()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set klik listener untuk tombol register
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                registerViewModel.register(name, email, password)
            }
        }

        observeRegisterResult()


    }

    private fun observeRegisterResult() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { response ->
                response?.let {
                    if (!it.error!!) {
                        Toast.makeText(this@RegisterActivity, "Register berhasil: ${it.message}", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Register gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}