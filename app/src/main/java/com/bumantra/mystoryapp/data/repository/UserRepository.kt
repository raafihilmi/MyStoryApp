package com.bumantra.mystoryapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bumantra.mystoryapp.model.UserModel
import com.bumantra.mystoryapp.data.local.pref.UserPreference
import com.bumantra.mystoryapp.data.remote.response.ErrorResponse
import com.bumantra.mystoryapp.data.remote.response.auth.LoginResponse
import com.bumantra.mystoryapp.data.remote.response.auth.RegisterResponse
import com.bumantra.mystoryapp.data.remote.response.story.StoryResponse
import com.bumantra.mystoryapp.data.remote.retrofit.ApiService
import com.bumantra.mystoryapp.utils.Result
import com.bumantra.mystoryapp.utils.wrapEspressoIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(userModel: UserModel) {
        userPreference.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSesion()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.register(name, email, password)
            Log.d("Signup", "setupAction: $response")
            emit(Result.Success(response))

        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("Repository", "register user: $errorMessage ")
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        wrapEspressoIdlingResource {
            try {
                val response = apiService.login(email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.d("Repository", "register user: $errorMessage ")
                emit(Result.Error(errorMessage.toString()))
            }
        }
    }

    fun getStoriesWithLocation(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("Repository", "register user: $errorMessage ")
            emit(Result.Error(errorMessage.toString()))
        }
    }
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}