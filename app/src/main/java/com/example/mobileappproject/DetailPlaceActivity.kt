package com.example.mobileappproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileappproject.extensions.goToMainActivity
import com.example.mobileappproject.lists.PlaceStatics
import com.example.mobileappproject.lists.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.reflect.typeOf

class DetailPlaceActivity : AppCompatActivity() {

    lateinit var _db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_place)

        _db = FirebaseDatabase.getInstance(getString(R.string.firebase_realtime_database_url)).reference
        // Get parameter from last Activity
        val b = intent.extras
        if (b != null) { val id = b.getString("objectId")
            if (id != null) {
                Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
                _db.child(PlaceStatics.FIREBASE_PLACE).child(id).get().addOnSuccessListener {
                    Log.i("firebase", "Got value ${it.value}")
                }
            } else {
                Toast.makeText(this, "Cannot not find the object id", Toast.LENGTH_SHORT).show()
            }
        }



        findViewById<Button>(R.id.BtnBackToMain).setOnClickListener {
            goToMainActivity(this)
            finish()
        }
    }
}