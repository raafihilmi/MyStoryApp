package com.bumantra.mystoryapp.ui.story.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bumantra.mystoryapp.data.local.entity.StoryEntity
import com.bumantra.mystoryapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val repository: StoryRepository) : ViewModel() {
    val getAllStory: LiveData<PagingData<StoryEntity>> =
        repository.getAllStories().cachedIn(viewModelScope)

    fun getDetailStory(id: String) = repository.getDetailStory(id)

    fun uploadStory(
        multipartBody: MultipartBody.Part,
        requestBody: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) =
        repository.uploadStory(multipartBody, requestBody, lat, lon)
}