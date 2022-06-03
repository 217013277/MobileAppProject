package com.example.mobileappproject

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import android.provider.Settings
import android.util.Log
import android.view.View

class AddPlaceActivity : AppCompatActivity() {

    private lateinit var latitude: TextView
    private lateinit var longitude: TextView

    // member variables that hold location info
    private lateinit var mLocationProviderClient: FusedLocationProviderClient
    private var mLastLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null
    private var mGeocoder: Geocoder? = null

    var mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            mLastLocation = result.lastLocation
            latitude.text = mLastLocation!!.latitude.toString()
            longitude.text = mLastLocation!!.longitude.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        latitude = findViewById(R.id.latitudeValue)
        longitude = findViewById(R.id.longitudeValue)

        // LocationRequest sets how often etc the app receives location updates
        mLocationRequest = LocationRequest
            .create()
            .setInterval(10)
            .setFastestInterval(5)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        getCurrentLocation()

        findViewById<Button>(R.id.getLocationBtn).setOnClickListener {
            Toast.makeText(this, "Start to get location, please wait", Toast.LENGTH_LONG).show()
            getCurrentLocation()
        }
        findViewById<Button>(R.id.getAddressBtn).setOnClickListener { getAddress() }
    }

    private fun getCurrentLocation() {
        if (isPermissionsGranted()) {
            if (isLocationEnabled()) {
                //all permissions checked, get location code here
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }
                mLocationProviderClient.requestLocationUpdates(
                    mLocationRequest!!,
                    mLocationCallBack, Looper.getMainLooper()
                )
            }
        } else {
            //request enable location
            Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    private fun stopLocation() {
        val removeTask = mLocationProviderClient.removeLocationUpdates(mLocationCallBack)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Location Callback removed.")
            } else {
                Log.d(TAG, "Failed to remove Location Callback.")
            }
        }
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun isPermissionsGranted(): Boolean {
        Log.d("LocationService", "checkPermissions")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun isLocationEnabled(): Boolean {
        Log.d("LocationButton", "Check isLocationEnabled")
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getAddress() {
        val tvAddress = findViewById<TextView>(R.id.addressValue)

        mGeocoder = Geocoder(this)
        try {
            // Only 1 address is needed here.
            val addresses = mGeocoder!!.getFromLocation(
                mLastLocation!!.latitude, mLastLocation!!.longitude, 1
            )
            if (addresses.size == 1) {
                val address = addresses[0]
                val addressLines = StringBuilder()
                //see herehttps://stackoverflow
                // .com/questions/44983507/android-getmaxaddresslineindex-returns-0-for-line-1
                if (address.maxAddressLineIndex > 0) {
                    for (i in 0 until address.maxAddressLineIndex) {
                        addressLines.append(
                            """
                    ${address.getAddressLine(i)}
                    """.trimIndent()
                        )
                    }
                } else {
                    addressLines.append(address.getAddressLine(0))
                }
                tvAddress!!.text = addressLines.toString()
            } else {
                tvAddress!!.text = "WARNING! Geocoder returned more than 1 addresses!"
            }
            stopLocation()
        } catch (e: Exception) {
        }
    }
}