package com.example.metaltraveller.utils

import android.content.Context
import android.content.Intent
import com.example.metaltraveller.models.DataManager
import com.example.metaltraveller.Place
import com.example.metaltraveller.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

/* Testing some utility functions to be used elsewhere. */

class Utils {

    val db = Firebase.firestore

    fun logOut(context: Context) {
        FirebaseAuth.getInstance().signOut()

        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    fun populatePlaceListFromFirebase() {
        db.collection("Places")
            .addSnapshotListener {snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    DataManager.places.clear()
                    for (document in snapshot.documents) {
                        val place = document.toObject<Place>()
                        if (place != null) {
                            DataManager.places.add(place)
                        }
                    }
                }
            }
    }


}