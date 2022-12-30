package com.example.metaltraveller.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.metaltraveller.R
import com.example.metaltraveller.databinding.ActivityMainBinding
import com.example.metaltraveller.utils.Utils
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.storage.FirebaseStorage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailsActivity : AppCompatActivity() {

    lateinit var name : TextView
    lateinit var type : TextView
    lateinit var location : TextView
    lateinit var rating : TextView

    lateinit var browseButton : Button
    lateinit var uploadButton : Button
    lateinit var itemPhoto : ImageView
    lateinit var imageUri : Uri

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
        uploadButton = findViewById(R.id.uploadButton)
        itemPhoto = findViewById(R.id.itemPhoto)

        browseButton.setOnClickListener {
            pickImageFromGallery()
        }

        uploadButton.setOnClickListener {
            uploadImage()
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

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageRef = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                itemPhoto.setImageURI(null)
                Toast.makeText(this@DetailsActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(this@DetailsActivity, "Failed to upload", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            itemPhoto.setImageURI(imageUri)
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

