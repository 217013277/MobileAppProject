package com.example.mobileappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.BtnBack).setOnClickListener { finish() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
    this.googleMap = googleMap
        // Add a marker to target location and move the camera
        val bundle = intent.extras
        val lat = bundle?.getString("lat")
        val lon = bundle?.getString("lon")
        val name = bundle?.getString("name")
        val placeLocation = LatLng(lat!!.toDouble(), lon!!.toDouble())
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.addMarker(MarkerOptions().position(placeLocation).title("$name"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 18F))
    }
}