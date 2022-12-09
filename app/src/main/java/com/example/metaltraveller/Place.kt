package com.example.metaltraveller

import com.google.android.gms.maps.model.LatLng


// NOTE Each argument could be null so remember ? and null in the declaration.

class Place(val name : String? = null, val rating : Int? = null, val type : String? = null, val position : LatLng? = null) {
}