package com.bumantra.mystoryapp.ui.story.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumantra.mystoryapp.databinding.ActivityDetailStoryBinding
import com.bumantra.mystoryapp.ui.story.list.StoryViewModel
import com.bumantra.mystoryapp.ui.story.list.StoryViewModelFactory
import com.bumantra.mystoryapp.utils.Result
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val storyViewModel by viewModels<StoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id") ?: ""
        Log.d("DetailStory", "Get Id: $id")

        storyViewModel.getDetailStory(id).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.detailProgressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.detailProgressBar.visibility = View.GONE
                    val storyData = result.data
                    Log.d("DetailStory", "Get Data: $storyData")
                    binding.ivDetailPhoto.loadImage(storyData.photoUrl)
                    binding.tvDetailName.text = storyData.name
                    binding.tvDetailDescription.text = storyData.description
                }

                is Result.Error -> {
                    binding.detailProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Error ${result.error} : Cek internet anda!", Toast.LENGTH_SHORT).show()
                    Log.d("DetailStory", "onCreate: ${result.error}")
                }
            }
        }

    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .into(this)
    }
}