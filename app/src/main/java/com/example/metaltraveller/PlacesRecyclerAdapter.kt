package com.example.metaltraveller

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context

class PlacesRecyclerAdapter(val context : Context, val places: List<Place>) : RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.recycled_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]

        holder.nameTextView.text = place.name
        holder.typeNameView.text = place.type
        holder.ratingTextView.text = place.rating.toString()
    }

    override fun getItemCount(): Int = places.size

    fun removePlace(position : Int) {
        DataManager.places.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.placeNameView)
        val typeNameView = itemView.findViewById<TextView>(R.id.placeTypeView)
        val ratingTextView = itemView.findViewById<TextView>(R.id.placeRatingView)
        val placeLocationView = itemView.findViewById<TextView>(R.id.placeLocationView)
        var favoriteButton = itemView.findViewById<CheckBox>(R.id.checkBox)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
        var placePosition = 0

        init {

            itemView.setOnClickListener() {
                val intent = Intent(context, CreateAndEditPlaceActivity::class.java)
                intent.putExtra(PLACE_POSITION_KEY, placePosition)
                context.startActivity(intent)

            }


            favoriteButton.setOnClickListener() {
                DataManager.favorites[placePosition].favorite = favoriteButton.isChecked
            }

            deleteButton.setOnClickListener() {
                removePlace(placePosition)
            }
        }
    }

}