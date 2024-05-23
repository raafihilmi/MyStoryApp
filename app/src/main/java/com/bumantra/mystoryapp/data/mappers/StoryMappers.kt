package com.bumantra.mystoryapp.data.mappers

import com.bumantra.mystoryapp.data.local.entity.StoryEntity
import com.bumantra.mystoryapp.data.remote.response.story.ListStoryItem

fun ListStoryItem.toStoryEntity(): StoryEntity {
    return StoryEntity(
        id = id,
        photoUrl = photoUrl,
        name = name,
        description = description,
        createdAt = createdAt,
        lon = lon,
        lat = lat
    )
}
