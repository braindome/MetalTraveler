package com.example.metaltraveller


import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId


// NOTE Each argument could be null so remember ? and null in the declaration.

class Place(
    var name : String? = null,
    var rating : Int? = null,
    var type : String? = null,
    var position : LatLng? = null,
    var location : String? = null,
    var favorite : Boolean = false,
    var expandable : Boolean = false,
    @DocumentId
    var documentId : String? = null) {
    constructor() : this("", 0, "", LatLng(0.0,0.0),
                        "", false, false, null
    )
}

val place = Place()