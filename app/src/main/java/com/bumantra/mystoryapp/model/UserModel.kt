package com.bumantra.mystoryapp.model

data class UserModel(
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)
