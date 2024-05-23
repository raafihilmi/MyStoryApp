package com.bumantra.mystoryapp.di

import android.content.Context
import com.bumantra.mystoryapp.data.repository.UserRepository
import com.bumantra.mystoryapp.data.local.pref.UserPreference
import com.bumantra.mystoryapp.data.local.pref.dataStore
import com.bumantra.mystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSesion().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}