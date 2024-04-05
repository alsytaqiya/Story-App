package com.dicoding.picodiploma.storyappsubmission.userinterface.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.storyappsubmission.R
import com.dicoding.picodiploma.storyappsubmission.data.modeldata.LoginDataClass
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseOfResult
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityMapsOfStoryBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.FactoryOfViewModel
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelOfMapsStory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsOfStory : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsOfStoryBinding
    private lateinit var loginUser: LoginDataClass
    private val mapsStoryViewModel: ViewModelOfMapsStory by viewModels {
        FactoryOfViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsOfStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginUser = intent.getParcelableExtra(EXTRA_USER)!!

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.story_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.style_of_map)
        )

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        showStoryData()
        getLocation()

    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showStoryData() {
        val builder = LatLngBounds.Builder()
        mapsStoryViewModel.getStory(loginUser.token).observe(this) {
            if (it != null) {
                when (it) {
                    is ResponseOfResult.Loading -> {
                    }
                    is ResponseOfResult.Success -> {
                        it.data.forEachIndexed { _, element ->
                            val lastLatLng = LatLng(element.lat, element.lon)

                            mMap.addMarker(
                                MarkerOptions().position(lastLatLng).title(element.name)
                                    .snippet(element.description)
                            )
                            builder.include(lastLatLng)
                            val bounds: LatLngBounds = builder.build()
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
                        }
                    }
                    is ResponseOfResult.Error -> {
                        Toast.makeText(this, getString(R.string.error_happened), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_USER = "user"
    }

}