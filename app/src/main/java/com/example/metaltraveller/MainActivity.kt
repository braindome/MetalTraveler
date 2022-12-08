package com.example.metaltraveller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val metalPlaces = mutableListOf<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore

//        val item1 = Place("Rockbaren", 1, "Bar")
//        val item2 = Place("Pustervik", 3, "Venue")
//
//        db.collection("Places").add(item1)
//        db.collection("Places").add(item2)

        val docRef = db.collection("Places")

//        docRef.get().addOnSuccessListener { documentSnapShot ->
//            for (document in documentSnapShot.documents) {
//                val item = document.toObject<Place>()
//                if (item != null) {
//                    Log.d("if-statement", "inside if statement")
//                    metalPlaces.add(item)
//                }
//
//            }
//            printPlaces()
//        }

        docRef.addSnapshotListener() { snapshot, e ->
            if (snapshot != null) {
                for (document in snapshot.documents) {
                    val item = document.toObject<Place>()
                    if (item != null) {
                        metalPlaces.add(item)
                    }
                }
            }
            printPlaces()

        }
    }

    fun printPlaces() {
        for (item in metalPlaces) {
            Log.d("!!!", "${item.name}")
        }
    }
}