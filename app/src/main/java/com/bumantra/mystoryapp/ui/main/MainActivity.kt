package com.bumantra.mystoryapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumantra.mystoryapp.R
import com.bumantra.mystoryapp.databinding.ActivityMainBinding
import com.bumantra.mystoryapp.ui.ViewModelFactory
import com.bumantra.mystoryapp.ui.auth.welcome.WelcomeActivity
import com.bumantra.mystoryapp.ui.map.MapsActivity
import com.bumantra.mystoryapp.ui.story.add.AddStoryActivity
import com.bumantra.mystoryapp.ui.story.list.LoadingStateAdapter
import com.bumantra.mystoryapp.ui.story.list.StoryAdapter
import com.bumantra.mystoryapp.ui.story.list.StoryViewModel
import com.bumantra.mystoryapp.ui.story.list.StoryViewModelFactory

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val storyViewModel by viewModels<StoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyAdapter = StoryAdapter()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.topAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.action_logout -> {
                    actionLogout()
                    true
                }

                R.id.action_maps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }

        }
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                binding.progressBar.visibility = View.VISIBLE
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                storyViewModel.getAllStory.observe(this) {
                    storyAdapter.submitData(lifecycle, it)
                }
            }
        }

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    private fun actionLogout() {
        AlertDialog.Builder(this).apply {
            setTitle("Logout")
            setMessage("Apakah kamu yakin ingin logout?")
            setPositiveButton("Ya") { _,_ ->
                viewModel.logout()
            }
            setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }


}