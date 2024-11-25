package id.co.mondo.storyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.co.mondo.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val name = intent.getStringExtra("STORY_NAME")
        val description = intent.getStringExtra("STORY_DESCRIPTION")
        val photoUrl = intent.getStringExtra("STORY_PHOTO")

        // Set data ke view
        if (name != null) {
            binding.tvDetailName.text = name
        } else {
            binding.tvDetailName.text = "Nama tidak tersedia"
        }

        if (description != null) {
            binding.tvDetailDescription.text = description
        } else {
            binding.tvDetailDescription.text = "Deskripsi tidak tersedia"
        }

        // Gunakan Glide untuk memuat gambar
        Glide.with(this)
            .load(photoUrl)
            .into(binding.ivDetailPhoto)
    }
}