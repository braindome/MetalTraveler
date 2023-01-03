package com.example.metaltraveller.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.metaltraveller.*
import com.example.metaltraveller.adapters.PlacesRecyclerAdapter
import com.example.metaltraveller.models.DataManager
import com.example.metaltraveller.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecycleListActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_list)

        setSupportActionBar(findViewById(R.id.detailsToolbar))


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlacesRecyclerAdapter(this, DataManager.places)

        Utils().populatePlaceListFromFirebase()

        val addButton = findViewById<FloatingActionButton>(R.id.floatAddButton)

        addButton.setOnClickListener {
            val intent = Intent(this, CreateAndEditPlaceActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Utils().logOut(this)
                true
            }
            R.id.action_map-> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


}