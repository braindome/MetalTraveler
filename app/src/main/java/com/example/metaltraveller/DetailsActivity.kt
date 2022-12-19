package com.example.metaltraveller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DetailsActivity : AppCompatActivity() {

    lateinit var name : TextView
    lateinit var type : TextView
    lateinit var location : TextView
    lateinit var rating : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val back : Button = findViewById(R.id.backButton)

        name = findViewById(R.id.nameText)
        type = findViewById(R.id.typeText)
        location = findViewById(R.id.locationText)
        rating = findViewById(R.id.ratingText)

        name.text = intent.getStringExtra("name")
        type.text = intent.getStringExtra("type")
        location.text = intent.getStringExtra("location")
        rating.text = intent.getIntExtra("rating", 0).toString()

        back.setOnClickListener {
            backToList()
        }
    }

    fun backToList() {
        finish()
    }
}

