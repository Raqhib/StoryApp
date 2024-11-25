package id.co.mondo.storyapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.mondo.storyapp.databinding.ItemListBinding
import id.co.mondo.storyapp.network.response.ListStoryItem

class StoryAdapter(
    private val onItemClick: (ListStoryItem) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private val stories = mutableListOf<ListStoryItem>()

    fun setStories(newStories: List<ListStoryItem>) {
        Log.d("StoryAdapter", "Data diterima adapter: ${stories.size} cerita")
        stories.clear()
        stories.addAll(newStories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            binding.tvItemDeskripsi.text = story.description

            // Menggunakan Glide untuk memuat gambar
            Glide.with(binding.ivItemPhoto.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            binding.root.setOnClickListener {
                onItemClick(story) // Panggil lambda dengan item yang diklik
            }
        }
    }
}
