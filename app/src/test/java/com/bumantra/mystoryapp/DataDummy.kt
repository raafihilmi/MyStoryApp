package com.bumantra.mystoryapp

import com.bumantra.mystoryapp.data.local.entity.StoryEntity

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                i.toString(),
                "photoUri + $i",
                "name $i",
                "description $i",
                null,
                null,
                null
            )
            items.add(story)
        }
        return items
    }

}