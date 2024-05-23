package com.bumantra.mystoryapp.di

import android.content.Context
import com.bumantra.mystoryapp.data.local.pref.UserPreference
import com.bumantra.mystoryapp.data.local.pref.dataStore
import com.bumantra.mystoryapp.data.local.room.StoryDatabase
import com.bumantra.mystoryapp.data.remote.retrofit.ApiConfig
import com.bumantra.mystoryapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSesion().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(storyDatabase, apiService)
    }
}