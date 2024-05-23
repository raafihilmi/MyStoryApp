package com.bumantra.mystoryapp.ui.map

import androidx.lifecycle.ViewModel
import com.bumantra.mystoryapp.data.repository.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel(){
    fun getStoriesWithLocation() = repository.getStoriesWithLocation()
}