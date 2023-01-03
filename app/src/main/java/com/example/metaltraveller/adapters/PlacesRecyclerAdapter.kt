package com.example.metaltraveller.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.metaltraveller.activities.DetailsActivity
import com.example.metaltraveller.activities.MapsActivity
import com.example.metaltraveller.models.DataManager
import com.example.metaltraveller.Place
import com.example.metaltraveller.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PlacesRecyclerAdapter(context : Context, val places: List<Place>)
                            : RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)
    val db = Firebase.firestore
    val auth = Firebase.auth
    val docRefPlaces = db.collection("Places")
    val context = this


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.recycled_item_layout, parent, false)

        return ViewHolder(itemView, parent.context)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]

        holder.nameTextView.text = place.name
        holder.typeNameView.text = place.type
        holder.ratingTextView.rating = place.rating!!
        holder.placeLocationView.text = place.location
        holder.placePosition = position
        "Place added by: ${place.userId}".also { holder.addedBy.text = it }

        val holdPlacePosition = holder.deleteButton

        val isExpandable : Boolean = place.expandable
        holder.expandedRow.visibility = if (isExpandable) View.VISIBLE else View.GONE





        holdPlacePosition.setOnClickListener {
            holder.removePlace(position)
            Log.d("holdPlace", "$position")
        }

        holder.itemView.setOnClickListener {
            val place = places[position]
            place.expandable = !place.expandable
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = places.size



    inner class ViewHolder(itemView : View, val context: Context) : RecyclerView.ViewHolder(itemView) {


        val nameTextView = itemView.findViewById<TextView>(R.id.placeNameView)
        val typeNameView = itemView.findViewById<TextView>(R.id.placeTypeView)
        val ratingTextView = itemView.findViewById<RatingBar>(R.id.recycleRatingView)
        val placeLocationView = itemView.findViewById<TextView>(R.id.placeLocationView)
        //var favoriteButton = itemView.findViewById<SwitchCompat>(R.id.switch1)
        val deleteButton = itemView.findViewById<AppCompatImageButton>(R.id.deleteButton)
        val detailsButton = itemView.findViewById<AppCompatTextView>(R.id.detailsButton)
        val mapButton = itemView.findViewById<AppCompatImageView>(R.id.mapButton)
        val addedBy = itemView.findViewById<TextView>(R.id.addedByView)


        var expandedRow = itemView.findViewById<RelativeLayout>(R.id.expandedRowLayout)
        var placePosition = 0

        fun toastNoAuth() {
            val message = "Not allowed to delete another user's item."
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun removePlace(position : Int) {
            DataManager.places[position].documentId?.let { removeFromFirestore(it, position) }
        }

        fun removeFromFirestore(documentId : String, position: Int) {
            if (auth.currentUser?.uid != places[position].userId) {
                toastNoAuth()
            } else {
                docRefPlaces.document(documentId).delete()
                    .addOnSuccessListener {
                        Log.d("itemRemoval", "DocumentSnapshot successfully deleted!")
                        notifyDataSetChanged()
                    }
                    .addOnFailureListener {
                        Log.d("itemRemoval", "Document not deleted.")
                    }
            }
        }


        init {

            detailsButton.setOnClickListener {
                val intent = Intent(itemView.context, DetailsActivity::class.java)
                intent.putExtra("name", places[placePosition].name)
                intent.putExtra("type", places[placePosition].type)
                intent.putExtra("location", places[placePosition].location)
                intent.putExtra("rating", places[placePosition].rating)
                intent.putExtra("image", places[placePosition].image)
                itemView.context.startActivity(intent)
            }

            mapButton.setOnClickListener {
                val intent = Intent(itemView.context, MapsActivity::class.java)
                itemView.context.startActivity(intent)
                //TODO: send stuff to MapsActivity
            }
        }

    }


}


