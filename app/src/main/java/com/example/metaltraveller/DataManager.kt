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
        places.add(Place("Trädgården", 5, "Venue", true))
        places.add(Place("Ullevi", 3, "Arena", false))
        places.add(Place("Anchor", 3, "Bar", true))
        places.add(Place("Fredagsmangel", 5, "Club"))
        places.add(Place("Truckstop Alaska", 5, "Club", true))
        places.add(Place("Brutal Assault", 5, "Festival", true))
    }
}