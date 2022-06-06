package com.example.mobileappproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailPlaceActivity : AppCompatActivity() {
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
    }
}