package com.bumantra.mystoryapp.ui.map

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumantra.mystoryapp.R
import com.bumantra.mystoryapp.databinding.ActivityMapsBinding
import com.bumantra.mystoryapp.ui.ViewModelFactory
import com.bumantra.mystoryapp.utils.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        setMapStyle()
        viewModel.getStoriesWithLocation().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("MapLoad", "onMapReady: Loading")
                    binding.mapsProgressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.mapsProgressBar.visibility = View.GONE
                    val data = result.data.listStory
                    data.forEach { userData ->
                        val latLng = LatLng(userData.lat, userData.lon)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(userData.name)
                                .snippet(userData.description)
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    }
                }

                is Result.Error -> {
                    binding.mapsProgressBar.visibility = View.GONE
                    Log.d("MapError", "onMapReady: ${result.error}")
                }
            }

        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("SetMapStyle", "setMapStyle: failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("SetMapStyle", "Error : ", e)
        }
    }
}