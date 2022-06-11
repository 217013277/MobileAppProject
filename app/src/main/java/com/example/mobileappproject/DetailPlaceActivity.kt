package com.example.mobileappproject

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileappproject.lists.PlaceStatics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class DetailPlaceActivity : AppCompatActivity() {

    private lateinit var detailLat: String
    private lateinit var detailLon: String
    private lateinit var imageUrl: String
    private lateinit var _db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_place)

        val detailMainImage = findViewById<ImageView>(R.id.ivMainImage)
        val detailTime = findViewById<TextView>(R.id.tvDetailTime)
        val detailName = findViewById<TextView>(R.id.tvDetailName)
        val detailDesc = findViewById<TextView>(R.id.tvDetailDesc)
        val detailAddress = findViewById<TextView>(R.id.tvDetailAddress)
        val detailWeather = findViewById<TextView>(R.id.tvDetailWeather)

        _db = FirebaseDatabase.getInstance(getString(R.string.firebase_realtime_database_url)).reference
        // Get parameter from last Activity
        val b = intent.extras
        if (b != null) { val id = b.getString("objectId")
            if (id != null) {
                _db.child(PlaceStatics.FIREBASE_PLACE).child(id).get().addOnSuccessListener {
                    detailTime.text = it.child("placeTime").value.toString()
                    detailName.text = it.child("placeName").value.toString()
                    detailDesc.text = it.child("placeDesc").value.toString()
//                    detailAddress.text = it.child("placeAddress").value.toString()
                    detailWeather.text = it.child("placeWeather").value.toString()
                    detailLat = it.child("placeLatitude").value.toString()
                    detailLon = it.child("placeLongitude").value.toString()
                    imageUrl = it.child("imageUrl").value.toString()
                    //decor text with underline
                    val address = it.child("placeAddress").value.toString()
                    // Creating a Spannable String
                    // from the above string
                    val addressUnderline = SpannableString(address)
                    // Setting underline style from
                    // position 0 till length of
                    // the spannable string
                    addressUnderline.setSpan(UnderlineSpan(), 0, addressUnderline.length, 0)
                    detailAddress.text = addressUnderline
                    detailAddress.setOnClickListener {
                        val intent = Intent(this,MapActivity::class.java)
                        val locationBundle = Bundle()
                        locationBundle.putString("lat", detailLat)
                        locationBundle.putString("lon", detailLon)
                        locationBundle.putString("name", detailName.text.toString())
                        intent.putExtras(locationBundle)
                        startActivity(intent)
                    }

                    if (imageUrl.isNotEmpty()){
                        Picasso.get().load(imageUrl).into(detailMainImage)
                    } else {
                        Picasso.get().load(R.drawable.placeholder_image_square).into(detailMainImage)
                    }

                    Log.i("firebase", "Got value ${it.value}")
                }
            } else {
                Toast.makeText(this, "Cannot not find the object id", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.BtnBackToMain).setOnClickListener {
            finish()
        }
    }
}