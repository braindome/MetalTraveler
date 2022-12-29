package com.example.metaltraveller.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.metaltraveller.R
import com.example.metaltraveller.databinding.ActivityMainBinding
import com.example.metaltraveller.utils.Utils
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class DetailsActivity : AppCompatActivity() {

    lateinit var name : TextView
    lateinit var type : TextView
    lateinit var location : TextView
    lateinit var rating : TextView
    lateinit var imageUrls : ArrayList<String>

    lateinit var browseButton : Button
    lateinit var itemPhoto : ImageView

    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    // Location stuff
    private val REQUEST_LOCATION = 1
    lateinit var locationProvider : FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    lateinit var locationCallback : LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val back : Button = findViewById(R.id.backButton)
        setSupportActionBar(findViewById(R.id.detailsToolbar))
        browseButton = findViewById(R.id.browseButton)
        itemPhoto = findViewById(R.id.itemPhoto)

        browseButton.setOnClickListener {
            pickImageFromGallery()
        }


        // Location stuff
        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(2000).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult : LocationResult) {
                for (location in locationResult.locations) {
                    Log.d("!!!", "lat: ${location.latitude}, lng: ${location.longitude}")
                }
            }
        }

        // Location stuff
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION)
        }

        name = findViewById(R.id.nameText)
        type = findViewById(R.id.typeText)
        location = findViewById(R.id.locationText)
        rating = findViewById(R.id.ratingText)

        name.text = intent.getStringExtra("name")
        type.text = intent.getStringExtra("type")
        location.text = intent.getStringExtra("location")
        rating.text = intent.getIntExtra("rating", 0).toString()

        back.setOnClickListener {
            backToList()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            itemPhoto.setImageURI(data?.data)
        }
    }

    fun backToList() {
        finish()
    }

    // Location stuff all the way down
    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_button -> {
                Utils().logOut(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    fun stopLocationUpdates() {
        locationProvider.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            }
        }
    }
}

