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
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.IOException

const val PLACE_POSITION_KEY = "PLACE_POSITION"
const val POSITION_NOT_SET = -1

class CreateAndEditPlaceActivity : AppCompatActivity() {

    lateinit var nameEditText : EditText
    lateinit var spinner : Spinner
    lateinit var ratingEditText: RatingBar
    lateinit var locationEditText : EditText
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage
    lateinit var imgUrl : EditText
    lateinit var addImg : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_and_edit_place)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
        setSupportActionBar(findViewById(R.id.detailsToolbar))

        val placePosition = intent.getIntExtra(PLACE_POSITION_KEY, POSITION_NOT_SET)
        val addButton = findViewById<Button>(R.id.addPlaceButton)

        val storageRef = storage.reference
        val pustervikRef = storageRef.child("images/pustervik.jpg")
        val rockbarenRef = storageRef.child("rockbaren.jpg")
        val ulleviRef = storageRef.child("ullevi.jpg")
        val abyssRef = storageRef.child("abyss.jpg")
        var imagesRef : StorageReference? = storageRef.child("Images")
        var spaceRef = storageRef.child("images/space.jpg")

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

        ArrayAdapter.createFromResource(
            this,
            R.array.place_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        addImg.setOnClickListener {
            uploadLocalFile()
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

    fun uploadLocalFile() {
        //TODO: https://firebase.google.com/docs/storage
    }


    fun addNewPlace() {
        val name = nameEditText.text.toString()
        val rating = ratingEditText.rating
        val type = spinner.selectedItem.toString()
        val location = locationEditText.text.toString()
        //val user = intent.getStringExtra("userId")
        val user = auth.currentUser?.uid

        val googleLatLng = getLatLngFromAddress(this, location)
        val position = com.example.metaltraveller.MyLatLng(googleLatLng?.latitude, googleLatLng?.longitude)

        val intent = Intent(this, RecycleListActivity::class.java)
        val place = Place(name, rating, type, position, location, null, user)

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

        Utils().populatePlaceListFromFirebase()
        //DataManager.places.add(place)
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
