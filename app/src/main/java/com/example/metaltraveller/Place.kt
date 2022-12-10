package com.example.metaltraveller

import com.google.android.gms.maps.model.LatLng


// NOTE Each argument could be null so remember ? and null in the declaration.

class Place(
    var name : String? = null,
    var rating : Int? = null,
    var type : String? = null,
    //val position : LatLng? = null,
    var favorite : Boolean? = false) {
}