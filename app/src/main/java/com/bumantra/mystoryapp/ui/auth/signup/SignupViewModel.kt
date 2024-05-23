package com.bumantra.mystoryapp.ui.auth.signup

import androidx.lifecycle.ViewModel
import com.bumantra.mystoryapp.data.repository.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) =
        repository.registerUser(name, email, password)

}