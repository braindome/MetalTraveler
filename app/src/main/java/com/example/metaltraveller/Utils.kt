package com.example.metaltraveller

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class Utils {
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
}