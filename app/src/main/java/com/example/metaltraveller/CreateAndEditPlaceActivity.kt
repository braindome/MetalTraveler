package com.example.metaltraveller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.LatLng

const val PLACE_POSITION_KEY = "PLACE_POSITION"
const val POSITION_NOT_SET = -1

class CreateAndEditPlaceActivity : AppCompatActivity() {

    lateinit var nameEditText : EditText
    lateinit var typeEditText: EditText
    lateinit var ratingEditText: EditText
    lateinit var coordinateEditText: EditText
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_and_edit_place)
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
        coordinateEditText = findViewById(R.id.coordinatesEdit)

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
//        val coordinates : LatLng = coordinateEditText.text.toString()
        val intent = Intent(this, RecycleListActivity::class.java)

        val place = Place(name, rating, type)
        DataManager.places.add(place)
        db.collection("Places").add(place)

        //finish()
        startActivity(intent)
    }
}