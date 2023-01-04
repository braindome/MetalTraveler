package com.example.metaltraveller

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import com.google.firebase.storage.UploadTask
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.metaltraveller.activities.MapsActivity
import com.example.metaltraveller.activities.RecycleListActivity
import com.example.metaltraveller.adapters.PlacesInfoAdapter
import com.example.metaltraveller.models.MyLatLng
import com.example.metaltraveller.utils.Utils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.util.Executors
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors.newSingleThreadExecutor

const val PLACE_POSITION_KEY = "PLACE_POSITION"
const val POSITION_NOT_SET = -1

class CreateAndEditPlaceActivity : AppCompatActivity() {

    lateinit var nameEditText : EditText
    lateinit var spinner : Spinner
    lateinit var ratingEditText: RatingBar
    lateinit var locationEditText : EditText
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage
    lateinit var uploadButton : Button
    lateinit var addImg : Button
    lateinit var itemImage : ImageView
    lateinit var imageUri : Uri
    lateinit var imageFileName : String
    var imageUrl : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_and_edit_place)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
        setSupportActionBar(findViewById(R.id.detailsToolbar))

        val placePosition = intent.getIntExtra(PLACE_POSITION_KEY, POSITION_NOT_SET)
        val addButton = findViewById<Button>(R.id.addPlaceButton)


        nameEditText = findViewById(R.id.placeNameEdit)
        spinner = findViewById(R.id.spinner)
        ratingEditText = findViewById(R.id.placeRatingEdit) as RatingBar
        locationEditText = findViewById(R.id.locationEdit)

        addImg = findViewById(R.id.addImageButton)
        uploadButton = findViewById(R.id.uploadImageButton)
        itemImage = findViewById(R.id.itemImage)

        itemImage.setImageResource(R.drawable.title_card_3)

        ArrayAdapter.createFromResource(
            this,
            R.array.place_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        addImg.setOnClickListener {
            pickImageFromGallery()
        }

        uploadButton.setOnClickListener {
            uploadImage()
        }

        addButton.setOnClickListener() {
            addNewPlace()
        }
    }

//    private fun uploadImage() {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Uploading...")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
//
//        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
//        val now = Date()
//        val fileName = formatter.format(now)
//        val storageRef = FirebaseStorage.getInstance().getReference("images/$fileName")
//
//
//        storageRef.putFile(imageUri)
//            .addOnSuccessListener {
//                getImageUrl(imageUri, fileName)
//                    .addOnSuccessListener { uri ->
//                        imageUri.toString() }
//                    .addOnFailureListener {
//                        Log.d("!!!", "fail") }
//                itemImage.setImageURI(null)
//                Toast.makeText(this@CreateAndEditPlaceActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
//                if (progressDialog.isShowing) progressDialog.dismiss()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this@CreateAndEditPlaceActivity, "Failed to upload", Toast.LENGTH_SHORT).show()
//                if (progressDialog.isShowing) progressDialog.dismiss()
//            }
//    }

    fun uploadImage(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageRef = FirebaseStorage.getInstance().getReference("images/$fileName")

        imageFileName = fileName.toString()
        imageUrl = "images/$imageFileName"


        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                itemImage.setImageURI(null)
                Toast.makeText(this@CreateAndEditPlaceActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(this@CreateAndEditPlaceActivity, "Failed to upload", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }

    }

    fun getImageUrl(imageUri : Uri, fileName : String) : Task<String> {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        return storageRef.child("images/$fileName").downloadUrl
            .continueWith { task ->
                task.result!!.toString()
            }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            itemImage.setImageURI(imageUri)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Utils().logOut(this)
                finish()
                true
            }
            R.id.action_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true

            }
            else -> super.onOptionsItemSelected(item)
        }

    }



    fun addNewPlace() {
        val name = nameEditText.text.toString()
        val rating = ratingEditText.rating
        val type = spinner.selectedItem.toString()
        val location = locationEditText.text.toString()
        val image = imageUrl

        val docRef = FirebaseFirestore.getInstance().collection("Places").document()



        //val user = intent.getStringExtra("userId")
        val user = auth.currentUser?.uid

        val googleLatLng = getLatLngFromAddress(this, location)
        val position = MyLatLng(googleLatLng?.latitude, googleLatLng?.longitude)

        val intent = Intent(this, RecycleListActivity::class.java)
        //val infoSquareIntent = Intent(this, PlacesInfoAdapter::class.java)
        val place = Place(name, rating, type, position, location, image, user)

        docRef.update("imageUrl", imageUrl)
            .addOnSuccessListener {
                // Update successful
            }
            .addOnFailureListener { e ->
                // Update failed
            }



        db.collection("Places").add(place)
            .addOnSuccessListener { documentReference ->
                Log.d("docId", "DocumentSnapshot written with ID: ${documentReference.id}")
                val placeId = documentReference.id
                place.documentId = placeId
                Log.d("docRef ID", place.documentId!!)
            }
            .addOnFailureListener { e ->
                Log.d("docId", "Error adding document")
            }

        ratingEditText.setOnRatingBarChangeListener { ratingEditText, rating, fromUser ->
            place.rating = rating
        }

        Utils().populatePlaceListFromFirebase()
        //DataManager.places.add(place)
        //Log.d("Pos!!!", "GeoPoint values: $position")
        startActivity(intent)
    }


    fun getLatLngFromAddress(context: Context, address: String?): LatLng? {
        if (address == null) {
            return null
        }
        val coder = Geocoder(context)
        val addressList = try {
            coder.getFromLocationName(address, 5)
        } catch (e: IOException) {
            return null
        }
        if (addressList != null) {
            if (addressList.isEmpty()) {
                return null
            }
        }
        val location = addressList?.get(0)
        if (location != null) {
            return LatLng(location.latitude, location.longitude)
        } else {
            return null
        }
    }
}

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}
