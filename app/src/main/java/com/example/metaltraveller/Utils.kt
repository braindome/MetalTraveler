package com.example.metaltraveller

import com.google.firebase.auth.FirebaseAuth

class Utils {
    fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }
}