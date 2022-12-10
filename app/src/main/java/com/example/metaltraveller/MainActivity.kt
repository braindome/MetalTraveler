package com.example.metaltraveller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase



class MainActivity : AppCompatActivity() {

    val metalPlaces = mutableListOf<Place>()

    lateinit var auth : FirebaseAuth
    lateinit var emailView : EditText
    lateinit var passwordView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore

        auth = Firebase.auth
        emailView = findViewById(R.id.emailEditText)
        passwordView = findViewById(R.id.passwordEditText)

        // Rockbaren coordinates 57.70 11.97
        // Pustervik coordinates 57-70 11.95

        val item1 = Place("Rockbaren", 1, "Bar")
        val item2 = Place("Pustervik", 3, "Venue")

        db.collection("Places").add(item1)
        db.collection("Places").add(item2)

        val docRef = db.collection("Places")

        val signUpButton = findViewById<Button>(R.id.signUpBtn)
        val signInButton = findViewById<Button>(R.id.signInBtn)

        signUpButton.setOnClickListener() {
            signUp()
        }

        signInButton.setOnClickListener() {
            signIn()
        }

        if (auth.currentUser != null) {
            goToAddActivity()
        }

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

    fun signIn() {
        val email = emailView.text.toString()
        val password = passwordView.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("!!!", "Signed in")
                goToAddActivity()
            } else {
                Log.d("!!!", "User is not signed in ${task.exception}")
            }

        }
    }

    fun signUp() {
        val email = emailView.text.toString()
        val password = passwordView.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("!!!", "Create success")
                goToAddActivity()
            } else {
                Log.d("!!!", "User is not created ${task.exception}")
            }

        }
    }

    fun goToAddActivity() {
        val intent = Intent(this, CreateAndEditPlaceActivity::class.java)
        startActivity(intent)
    }
}