package com.example.metaltraveller


//import com.google.android.gms.maps.model.LatLng
import android.graphics.Bitmap
import com.example.metaltraveller.models.MyLatLng
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
    val userId: String? = null,
    var favorite: Boolean = false,
    var expandable: Boolean = false,
    @DocumentId
    var documentId: String? = "",

    ) {
}

data class User(@DocumentId var userId : String? = null,
                            var email : String? = null) : Serializable {

}
