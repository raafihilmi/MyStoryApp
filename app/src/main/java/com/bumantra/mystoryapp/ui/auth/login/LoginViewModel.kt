package com.bumantra.mystoryapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumantra.mystoryapp.data.repository.UserRepository
import com.bumantra.mystoryapp.model.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(userModel: UserModel) {
        viewModelScope.launch {
            repository.saveSession(userModel)
        }
    }

    fun login(email: String, password: String) = repository.login(email, password)
}