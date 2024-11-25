package id.co.mondo.storyapp.ui.story

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.co.mondo.storyapp.network.response.FileUploadResponse
import id.co.mondo.storyapp.network.response.ListStoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableStateFlow<List<ListStoryItem>>(emptyList())
    val stories = _stories.asLiveData() // Konversi ke LiveData

    private val _uploadResult = MutableStateFlow<FileUploadResponse?>(null)
    val uploadResult = _uploadResult.asLiveData() // Konversi ke LiveData

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asLiveData() // Konversi ke LiveData

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asLiveData()

    fun fetchStories() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = storyRepository.getStories()

            if (result.isSuccess) {
                _stories.value = result.getOrDefault(emptyList())
                Log.d("StoryViewModel", "Berhasil mengambil cerita: ${_stories.value.size}")
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Terjadi kesalahan"
                _errorMessage.value = errorMessage
                Log.e("StoryViewModel", "Gagal mengambil cerita: $errorMessage")
            }

            _isLoading.value = false
        }
    }

    fun uploadStory(photo: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = storyRepository.uploadStory(photo, description)

            if (result.isSuccess) {
                _uploadResult.value = result.getOrNull()
                Log.d("StoryViewModel", "Cerita berhasil diunggah")
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Gagal mengunggah cerita"
                _errorMessage.value = errorMessage
                Log.e("StoryViewModel", "Gagal mengunggah cerita: $errorMessage")
            }

            _isLoading.value = false
        }
    }


    suspend fun clearToken() {
        storyRepository.clearToken()
    }
}

