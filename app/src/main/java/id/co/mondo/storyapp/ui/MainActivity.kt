package id.co.mondo.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.mondo.storyapp.R
import id.co.mondo.storyapp.databinding.ActivityMainBinding
import id.co.mondo.storyapp.ui.adapter.StoryAdapter
import id.co.mondo.storyapp.ui.auth.login.LoginActivity
import id.co.mondo.storyapp.ui.story.AddStoryActivity
import id.co.mondo.storyapp.ui.story.StoryViewModel
import id.co.mondo.storyapp.ui.story.StoryViewModelFactory
import id.co.mondo.storyapp.ui.utils.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory(this)
    }

    private val storyAdapter by lazy {
        StoryAdapter { storyItem ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("STORY_NAME", storyItem.name)
                putExtra("STORY_DESCRIPTION", storyItem.description)
                putExtra("STORY_PHOTO", storyItem.photoUrl)
            }
            startActivity(intent)
        }
    }


    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val userPreferences = UserPreferences.getInstance(this@MainActivity)
            val token = userPreferences.token.firstOrNull()
            if (token.isNullOrEmpty()) {
                Log.d("MainActivity", "Token tidak ditemukan. Arahkan ke Login.")
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        lifecycleScope.launch {
            val userPreferences = UserPreferences.getInstance(this@MainActivity)
            val userName = userPreferences.userName.firstOrNull()
            binding.textView.text = if (!userName.isNullOrEmpty()) {
                getString(R.string.welcome_user, userName)
            } else {
                getString(R.string.welcome_default)
            }
        }


        setupRecyclerView()
        observeViewModel()
        storyViewModel.fetchStories()

        binding.logoutBtn.setOnClickListener {
            logout()
        }

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

    }
    private fun setupRecyclerView() {
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun observeViewModel() {
        storyViewModel.stories.observe(this) { stories ->
            storyAdapter.setStories(stories) // Perbarui adapter dengan data cerita
        }

        storyViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        storyViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                showToast(it) // Tampilkan pesan error jika ada
            }
        }
    }


    private fun logout() {
        lifecycleScope.launch {
            val userPreferences = UserPreferences.getInstance(this@MainActivity)
            userPreferences.clearToken()
            userPreferences.clearUserName()
            Log.d("MainActivity", "Token dan nama pengguna berhasil dihapus")

            Toast.makeText(this@MainActivity, "Logout berhasil", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}