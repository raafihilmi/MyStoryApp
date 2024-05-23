package com.bumantra.mystoryapp.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumantra.mystoryapp.R
import com.bumantra.mystoryapp.databinding.ActivityAddStoryBinding
import com.bumantra.mystoryapp.ui.main.MainActivity
import com.bumantra.mystoryapp.ui.story.list.StoryViewModel
import com.bumantra.mystoryapp.ui.story.list.StoryViewModelFactory
import com.bumantra.mystoryapp.utils.Result
import com.bumantra.mystoryapp.utils.getImageUri
import com.bumantra.mystoryapp.utils.reduceFileImage
import com.bumantra.mystoryapp.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val storyViewModel by viewModels<StoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLat: Float? = null
    private var currentLog: Float? = null
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.cbLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLastLocation()
            } else {
                currentLog = null
                currentLat = null
            }
        }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { startUpload() }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }

                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLat = location.latitude.toFloat()
                    currentLog = location.longitude.toFloat()
                } else {
                    Toast.makeText(this, "Location tidak ada, yuk dicoba lagi", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("AddStory", "Tidak ada gambar yang dipilih")
        }
    }


    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentGallery.launch(currentImageUri)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startUpload() {
        currentImageUri?.let { uri ->
            val image = uriToFile(uri, this).reduceFileImage()
            val desc = binding.edAddDescription.text.toString()

            Log.d("Upload Check", "startUpload: $currentLog")

            binding.storyProgressBar.visibility = View.VISIBLE

            val requestBody = desc.toRequestBody("text/plain".toMediaType())
            val requestImage = image.asRequestBody("image/jpeg".toMediaType())
            val latRequestBody = if (currentLat != null) currentLat.toString().toRequestBody("text/plain".toMediaType()) else null
            val lonRequestBody = if (currentLog != null) currentLog.toString().toRequestBody("text/plain".toMediaType()) else null
            Log.d("Check Data", "startUpload: $latRequestBody")
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                image.name,
                requestImage,
            )
            storyViewModel.uploadStory(multipartBody, requestBody, latRequestBody, lonRequestBody)
                .observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.storyProgressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.storyProgressBar.visibility = View.GONE
                            val response = result.data.message
                            Log.d("AddStory", "startUpload: $response")
                            AlertDialog.Builder(this).apply {
                                setTitle("Upload berhasil")
                                setMessage("Story kamu telah berhasil diposting!")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }

                        is Result.Error -> {
                            binding.storyProgressBar.visibility = View.GONE
                            Log.d("AddStoryError", "startUpload: ${result.error}")
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }

                }
        } ?: Toast.makeText(this, R.string.empty_img_warning, Toast.LENGTH_SHORT).show()
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image", "showImage: $it")
            binding.ivStory.setImageURI(it)
        }
    }

}