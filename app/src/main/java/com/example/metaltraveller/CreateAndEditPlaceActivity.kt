package com.example.metaltraveller

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.ktx.Firebase
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import java.io.IOException

const val PLACE_POSITION_KEY = "PLACE_POSITION"
const val POSITION_NOT_SET = -1

class CreateAndEditPlaceActivity : AppCompatActivity() {

    lateinit var nameEditText : EditText
    lateinit var typeEditText: EditText
    lateinit var ratingEditText: EditText
    lateinit var locationEditText: EditText
    //val db = Firebase.firestore
    lateinit var db : FirebaseFirestore

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
        typeEditText = findViewById(R.id.placeTypeEdit)
        ratingEditText = findViewById(R.id.placeRatingEdit)
        locationEditText = findViewById(R.id.locationEdit)

        addButton.setOnClickListener() {

            addNewPlace()
        }
    }

    fun displayPlace(position: Int) {
        val place = DataManager.places[position]

        nameEditText.setText(place.name)
        typeEditText.setText(place.type)
        ratingEditText.setText(place.rating.toString())

        finish()
    }

    fun editPlace(position : Int) {
        DataManager.places[position].name = nameEditText.text.toString()
        DataManager.places[position].type = typeEditText.text.toString()
        DataManager.places[position].rating = ratingEditText.text.toString().toInt()

        finish()
    }

    fun addNewPlace() {
        val name = nameEditText.text.toString()
        val type = typeEditText.text.toString()
        val rating = ratingEditText.text.toString().toInt()
        val location = locationEditText.text.toString()
        val position = getLatLngFromAddress(this, location)

        val intent = Intent(this, RecycleListActivity::class.java)
        val place = Place(name, rating, type, position, location)

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
            coder.getFromLocationName(address, 1)
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