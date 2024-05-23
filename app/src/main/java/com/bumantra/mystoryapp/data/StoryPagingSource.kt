package com.bumantra.mystoryapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumantra.mystoryapp.data.local.entity.StoryEntity
import com.bumantra.mystoryapp.data.mappers.toStoryEntity
import com.bumantra.mystoryapp.data.remote.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryEntity>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
        val position = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val responseData = apiService.getStories(position, params.loadSize)
            val story = responseData.listStory
            val storyList = story.map { user->
                user.toStoryEntity()
            }
            val nextKey = if (story.isEmpty()) {
                null
            } else {
                position + (params.loadSize / 5)
            }

            LoadResult.Page(
                data = storyList,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
//                nextKey = if (story.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}