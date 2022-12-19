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
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import com.google.android.gms.common.data.DataHolder
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class PlacesRecyclerAdapter(context : Context,
                            val places: List<Place>)
                            : RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)
    val db = Firebase.firestore
    val docRefPlaces = db.collection("Places")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.recycled_item_layout, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]

        holder.nameTextView.text = place.name
        holder.typeNameView.text = place.type
        holder.ratingTextView.text = place.rating.toString()
        holder.placeLocationView.text = place.location
        holder.placePosition = position

        val holdPlacePosition = holder.deleteButton

        val isExpandable : Boolean = place.expandable
        holder.expandedRow.visibility = if (isExpandable) View.VISIBLE else View.GONE



        holdPlacePosition.setOnClickListener {
            removePlace(position)
            Log.d("holdPlace", "$position")
        }

        holder.itemView.setOnClickListener {
            val place = places[position]
            place.expandable = !place.expandable
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = places.size

    fun removePlace(position : Int) {

        DataManager.places[position].documentId?.let { removeFromFirestore(it, position) }
        //Log.d("Removed item id", DataManager.places[position].documentId!!)

    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.placeNameView)
        val typeNameView = itemView.findViewById<TextView>(R.id.placeTypeView)
        val ratingTextView = itemView.findViewById<TextView>(R.id.placeRatingView)
        val placeLocationView = itemView.findViewById<TextView>(R.id.placeLocationView)
        var favoriteButton = itemView.findViewById<CheckBox>(R.id.checkBox)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
        val detailsButton = itemView.findViewById<TextView>(R.id.detailsButton)


        var expandedRow = itemView.findViewById<RelativeLayout>(R.id.expandedRowLayout)
        var placePosition = 0


        init {

//            itemView.setOnClickListener() {
//                val intent = Intent(context, CreateAndEditPlaceActivity::class.java)
//                intent.putExtra(PLACE_POSITION_KEY, placePosition)
//                context.startActivity(intent)
//
//            }

            detailsButton.setOnClickListener {
                val intent = Intent(itemView.context, DetailsActivity::class.java)
                intent.putExtra("name", places[placePosition].name)
                intent.putExtra("type", places[placePosition].type)
                intent.putExtra("location", places[placePosition].location)
                intent.putExtra("rating", places[placePosition].rating)
                itemView.context.startActivity(intent)
            }

            favoriteButton.setOnClickListener() {
                DataManager.favorites[placePosition].favorite = favoriteButton.isChecked
            }

//            deleteButton.setOnClickListener() {
//                removePlace(placePosition)
//            }
        }

    }

    fun addToListFromDb() {
        docRefPlaces.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                DataManager.places.clear()
                for (document in snapshot.documents) {
                    val item = document.toObject<Place>()
                    if (item != null) {
                        DataManager.places.add(item)
                    }
                }
            }
        }
    }

    fun printPlaces() {
        for (item in DataManager.places) {
            Log.d("Place list!!!!", "${item.name}")
        }
    }

    fun removeFromFirestore(documentId: String, position: Int) {

        docRefPlaces.document(documentId).delete()
            .addOnSuccessListener {
                Log.d("itemRemoval", "DocumentSnapshot successfully deleted!")
                DataManager.places.removeAt(position)
                notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("itemRemoval", "Item not deleted")
            }


    }




}


