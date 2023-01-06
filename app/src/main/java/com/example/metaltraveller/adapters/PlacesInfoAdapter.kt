package com.example.metaltraveller.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.metaltraveller.Place
import com.example.metaltraveller.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PlacesInfoAdapter(val context: Context) : GoogleMap.InfoWindowAdapter {

    val layoutInflater = LayoutInflater.from(context)

    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker : Marker): View? {
        val infoWindow = layoutInflater.inflate(R.layout.info_window, null)

        val markerName = infoWindow.findViewById<TextView>(R.id.markerNameView)
        val markerType = infoWindow.findViewById<TextView>(R.id.markerTypeView)
        val markerRating = infoWindow.findViewById<RatingBar>(R.id.markerRatingView)
        val place = marker.tag as? Place

        markerName.text = place?.name
        markerType.text = place?.type
        markerRating.rating = place?.rating!!



        return infoWindow
    }
}