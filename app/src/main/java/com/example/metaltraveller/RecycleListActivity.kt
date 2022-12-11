package com.example.metaltraveller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecycleListActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlacesRecyclerAdapter(this, DataManager.places)

        val addButton = findViewById<FloatingActionButton>(R.id.floatAddButton)

        addButton.setOnClickListener {
            val intent = Intent(this, CreateAndEditPlaceActivity::class.java)
            startActivity(intent)
        }
    }
}