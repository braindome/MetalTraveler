package com.example.metaltraveller


//import com.google.android.gms.maps.model.LatLng
import android.graphics.Bitmap
import com.google.firebase.firestore.DocumentId
import java.io.Serializable


// NOTE Each argument could be null so remember ? and null in the declaration.

data class Place(
    var name: String? = "",
    var rating: Float? = null,
    var type: String? = "",
    var position: MyLatLng? = MyLatLng(0.0, 0.0),
    var location: String? = "",
    var image : Bitmap? = null,
    var favorite: Boolean = false,
    var expandable: Boolean = false,
    @DocumentId
    var documentId: String? = "",
    //val user : User? = null
                            ) {
}

data class User(@DocumentId var documentId : String? = null,
                            var email : String? = null) : Serializable {

}
