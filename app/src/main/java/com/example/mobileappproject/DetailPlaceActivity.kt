package com.example.mobileappproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailPlaceActivity : AppCompatActivity() {

    lateinit var _db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_place)
        val b = intent.extras
        if (b != null) { val value = b?.getString("objectId")
            if (value != null) {
                Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cannot not find the object id", Toast.LENGTH_SHORT).show()
            }
        }

        _db = FirebaseDatabase.getInstance(getString(R.string.firebase_realtime_database_url)).reference
    }
}