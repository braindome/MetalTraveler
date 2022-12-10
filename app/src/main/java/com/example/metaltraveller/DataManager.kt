package com.example.metaltraveller

import com.google.android.gms.maps.model.LatLng

object DataManager {

    val places = mutableListOf<Place>()
    val favorites = mutableListOf<Place>()

    init {
        createMockData()
    }

    fun createMockData() {
        //ADD PLACES TO List
        places.add(Place("Trädgården", 5, "Venue", LatLng(0.0, 0.0), true))
        places.add(Place("Ullevi", 3, "Arena", LatLng(0.0, 0.0), false))
        places.add(Place("Anchor", 3, "Bar", LatLng(0.0, 0.0), true))
        places.add(Place("Fredagsmangel", 5, "Club", LatLng(0.0, 0.0)))
        places.add(Place("Truckstop Alaska", 5, "Club", LatLng(0.0, 0.0), true))
        places.add(Place("Brutal Assault", 5, "Festival", LatLng(0.0, 0.0), true))
    }
}