package com.example.metaltraveller

import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.api.Context

class PlacesRecyclerAdapter(val context : Context, val places : List<Place>) :
    RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesRecyclerAdapter {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = places.size

    fun removePlace(position : Int) {
        DataManager.places.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) {
        val nameTextView = itemView.findViewById<TextView>(R.id.placeNameView)
        val typeNameView = itemView.findViewById<TextView>(R.id.placeTypeView)
        val ratingTextView = itemView.findViewById<TextView>(R.id.placeRatingView)
        val placeLocationView = itemView.findViewById<TextView>(R.id.placeLocationView)
        var favoriteButton = itemView.findViewById<CheckBox>(R.id.checkBox)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
        var placePosition = 0

        init {


            favoriteButton.setOnClickListener() {
                DataManager.favorites[placePosition].favorite = favoriteButton.isChecked
            }

            deleteButton.setOnClickListener() {
                removePlace(placePosition)
            }
        }
    }

}