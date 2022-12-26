package com.example.metaltraveller

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

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