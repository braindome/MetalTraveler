package com.example.metaltraveller.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.metaltraveller.Place
import com.example.metaltraveller.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class PlacesInfoAdapter(val context: Context) : GoogleMap.InfoWindowAdapter {

    val layoutInflater = LayoutInflater.from(context)

    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker : Marker): View? {
        val infoWindow = layoutInflater.inflate(R.layout.info_window, null)

        val markerName = infoWindow.findViewById<TextView>(R.id.markerNameView)
        val markerType = infoWindow.findViewById<TextView>(R.id.markerTypeView)
        val markerRating = infoWindow.findViewById<TextView>(R.id.markerRatingView)

        val place = marker.tag as? Place

        markerName.text = place?.name
        markerType.text = place?.type
        markerRating.text = place?.rating.toString()

        // For images check video


        return infoWindow
    }
}