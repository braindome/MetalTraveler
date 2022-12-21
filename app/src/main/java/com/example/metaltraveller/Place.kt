package com.example.metaltraveller


//import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId


// NOTE Each argument could be null so remember ? and null in the declaration.

data class Place(
    var name : String? = "",
    var rating : Int? = 0,
    var type : String? = "",
    var position : MyLatLng? = MyLatLng(0.0, 0.0),
    var location : String? = "",
    var favorite : Boolean = false,
    var expandable : Boolean = false,
    @DocumentId
    var documentId : String? = "") {
}
