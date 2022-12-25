package com.example.metaltraveller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException

const val PLACE_POSITION_KEY = "PLACE_POSITION"
const val POSITION_NOT_SET = -1

class CreateAndEditPlaceActivity : AppCompatActivity() {

    lateinit var nameEditText : EditText
    lateinit var spinner : Spinner
    lateinit var ratingEditText: RatingBar
    lateinit var locationEditText : EditText
    //val db = Firebase.firestore
    lateinit var db : FirebaseFirestore
    lateinit var imgUrl : EditText
    lateinit var addImg : Button
    lateinit var placeImage : ImageView
    lateinit var urlList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_and_edit_place)
        db = Firebase.firestore
        val placePosition = intent.getIntExtra(PLACE_POSITION_KEY, POSITION_NOT_SET)
        val addButton = findViewById<Button>(R.id.addPlaceButton)

//        if (placePosition != POSITION_NOT_SET) {
//            displayPlace(placePosition)
//            addButton.setOnClickListener() {
//                //TODO
//            }
//        }

        nameEditText = findViewById(R.id.placeNameEdit)
        spinner = findViewById(R.id.spinner)
        ratingEditText = findViewById(R.id.placeRatingEdit) as RatingBar
        locationEditText = findViewById(R.id.locationEdit)
        imgUrl = findViewById(R.id.imageUrlText)
        addImg = findViewById(R.id.addImageButton)
        placeImage = findViewById(R.id.placePrevievImageView)

        ArrayAdapter.createFromResource(
            this,
            R.array.place_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        addImg.setOnClickListener {
            addImageUrlToList()
        }

        addButton.setOnClickListener() {
            addNewPlace()
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
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    fun addImageUrlToList() {
        val url = imgUrl.text.toString()
        urlList.add(url)
        //val image = placeImage
        val image = findViewById<ImageView>(R.id.placePrevievImageView)

        Glide.with(this)
            .load(url)
            .into(image)

    }


    fun addNewPlace() {
        val name = nameEditText.text.toString()
        val rating = ratingEditText.rating
        val type = spinner.selectedItem.toString()
        val location = locationEditText.text.toString()
        val images = urlList

        val googleLatLng = getLatLngFromAddress(this, location)
        val position = com.example.metaltraveller.MyLatLng(googleLatLng?.latitude, googleLatLng?.longitude)

        val intent = Intent(this, RecycleListActivity::class.java)
        val place = Place(name, rating, type, position, location, images)

        db.collection("Places").add(place)
            .addOnSuccessListener { documentReference ->
                Log.d("docId", "DocumentSnapshot written with ID: ${documentReference.id}")
                val placeId = documentReference.id
                place.documentId = placeId
                Log.d("docRef ID", place.documentId!!)
            }
            .addOnFailureListener { e ->
                Log.d("docId", "Error adding document")
            }

        ratingEditText.setOnRatingBarChangeListener { ratingEditText, rating, fromUser ->
            place.rating = rating
        }


        DataManager.places.add(place)
        //Log.d("Pos!!!", "GeoPoint values: $position")
        startActivity(intent)
    }


    fun getLatLngFromAddress(context: Context, address: String?): LatLng? {
        if (address == null) {
            return null
        }
        val coder = Geocoder(context)
        val addressList = try {
            coder.getFromLocationName(address, 5)
        } catch (e: IOException) {
            return null
        }
        if (addressList != null) {
            if (addressList.isEmpty()) {
                return null
            }
        }
        val location = addressList?.get(0)
        if (location != null) {
            return LatLng(location.latitude, location.longitude)
        } else {
            return null
        }
    }
}

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}
