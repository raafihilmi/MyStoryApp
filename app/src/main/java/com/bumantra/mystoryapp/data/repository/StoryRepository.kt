package com.bumantra.mystoryapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bumantra.mystoryapp.data.StoryRemoteMediator
import com.bumantra.mystoryapp.data.local.entity.StoryEntity
import com.bumantra.mystoryapp.data.local.room.StoryDatabase
import com.bumantra.mystoryapp.data.remote.response.ErrorResponse
import com.bumantra.mystoryapp.data.remote.response.story.Story
import com.bumantra.mystoryapp.data.remote.response.story.UploadResponse
import com.bumantra.mystoryapp.data.remote.retrofit.ApiService
import com.bumantra.mystoryapp.utils.Result
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {

    fun getAllStories(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllQuote()
            }
        ).liveData

    }

    fun getDetailStory(id: String): LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(id).story
            Log.d("StoryRepository", "getDetailStory: $response")
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(
        multipartBody: MultipartBody.Part,
        requestBody: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): LiveData<Result<UploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.upload(multipartBody, requestBody, lat, lon)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase, apiService)
            }.also { instance = it }
    }
}