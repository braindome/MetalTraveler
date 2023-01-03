package com.example.metaltraveller.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.metaltraveller.models.DataManager
import com.example.metaltraveller.adapters.PlacesInfoAdapter
import com.example.metaltraveller.R
import com.example.metaltraveller.utils.Utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.metaltraveller.databinding.ActivityMapsBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var latLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.detailsToolbar))

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val adapter = PlacesInfoAdapter(this)
        mMap.setInfoWindowAdapter(adapter)
        createMarkers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Utils().logOut(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    fun createMarkers() {
        for (place in DataManager.places) {
            if (place.position != null) {
                val myLat = place.position!!.lat
                val myLng = place.position!!.lng

                latLng = LatLng(myLat!!, myLng!!)

                val marker = mMap.addMarker(MarkerOptions().position(latLng))
                marker?.tag = place

                Log.d("mark", "Marker added on ${latLng}")
            }

        }
    }


}
