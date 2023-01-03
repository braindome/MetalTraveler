package com.example.metaltraveller.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.metaltraveller.Place
import com.example.metaltraveller.R
import com.example.metaltraveller.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase



class MainActivity : AppCompatActivity() {

    val metalPlaces = mutableListOf<Place>()

    lateinit var auth : FirebaseAuth
    lateinit var emailView : EditText
    lateinit var passwordView : EditText
    lateinit var db : FirebaseFirestore
    lateinit var docRef : CollectionReference
    lateinit var usersRef : CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Firebase.firestore
        auth = Firebase.auth
        emailView = findViewById(R.id.emailEditText)
        passwordView = findViewById(R.id.passwordEditText)

        val user = User(null, auth.currentUser?.email)
        val firebaseUser = auth.currentUser

        docRef = db.collection("Places")
        usersRef = db.collection("Users")

        if (firebaseUser != null) {
            usersRef.document(firebaseUser.uid).set(user)
        }

        val signUpButton = findViewById<Button>(R.id.signUpBtn)
        val signInButton = findViewById<Button>(R.id.signInBtn)



        signUpButton.setOnClickListener() {
            signUp()
        }

        signInButton.setOnClickListener() {
            signIn()
        }


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
                //user.documentId?.let { user.email?.let { it1 -> addUserToDb(it, it1) } }
                goToAddActivity()
            } else {
                Log.d("!!!", "User is not created ${task.exception}")
            }

        }
    }

    fun goToAddActivity() {
        val intent = Intent(this, RecycleListActivity::class.java)
        intent.putExtra("userId", auth.uid)
        startActivity(intent)
    }

    fun addUserToDb(uid : String, email : String) {
        usersRef.add(User(uid, email))
            .addOnSuccessListener { documentReference ->
                Log.d("UserSnap", "DocumentSnapshot added with id: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.d("UserSnap", "User not added to database", e)
            }
    }
}