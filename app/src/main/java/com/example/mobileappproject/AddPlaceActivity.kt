package com.example.mobileappproject

import android.Manifest
import android.annotation.SuppressLint
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
import android.widget.EditText
import com.example.mobileappproject.extensions.goToBiometricActivity
import com.example.mobileappproject.extensions.goToMainActivity
import com.example.mobileappproject.lists.Place
import com.example.mobileappproject.lists.PlaceStatics
import com.example.mobileappproject.sharedPreferences.PostTemplate
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPlaceActivity : AppCompatActivity() {

    private lateinit var etPlaceName: EditText
    private lateinit var etPlaceDesc: EditText
    private lateinit var placeLatitude: TextView
    private lateinit var placeLongitude: TextView
    private lateinit var placeAddress: TextView

    private lateinit var _db: DatabaseReference

    // member variables that hold location info
    private lateinit var mLocationProviderClient: FusedLocationProviderClient
    private var mLastLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null
    private var mGeocoder: Geocoder? = null

    private var mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            mLastLocation = result.lastLocation
            placeLatitude.text = mLastLocation!!.latitude.toString()
            placeLongitude.text = mLastLocation!!.longitude.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        etPlaceName = findViewById(R.id.etPlaceName)
        etPlaceDesc = findViewById(R.id.etPlaceDesc)
        placeLatitude = findViewById(R.id.latitudeValue)
        placeLongitude = findViewById(R.id.longitudeValue)

        _db = FirebaseDatabase.getInstance("https://vtclab-da73a-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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
        findViewById<Button>(R.id.submitBtn).setOnClickListener { addPlace() }
        findViewById<Button>(R.id.goToMainBtn).setOnClickListener {
            goToMainActivity(this)
            finish()
        }
    }

    private fun addPlace() {
        //Declare and Initialise the Task
        val place = Place.create()
        //Set Task Description and isDone Status
        place.placeName = etPlaceName.text.toString()
        place.placeDesc = etPlaceDesc.text.toString()
        place.placeLatitude = placeLatitude.text.toString()
        place.placeLongitude = placeLongitude.text.toString()
        place.placeAddress = placeAddress.text.toString()
        place.isFav = false
        //Get the object id for the new task from the Firebase Database
        val newTask = _db.child(PlaceStatics.FIREBASE_TASK).push()
        place.objectId = newTask.key
        //Set the values for new task in the firebase using the footer form
        newTask.setValue(place).addOnSuccessListener {
            etPlaceName.setText("")
            etPlaceDesc.setText("")
            placeLatitude.text = ""
            placeLongitude.text = ""
            placeAddress.text = ""
            Toast.makeText(this, "Task added to the list successfully" + place.objectId, Toast.LENGTH_SHORT).show()
            goToMainActivity(this)
        }.addOnFailureListener {
            Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show()
        }
        //Reset the new task description field for reuse.
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

    @SuppressLint("SetTextI18n")
    private fun getAddress() {
        placeAddress = findViewById(R.id.addressValue)

        mGeocoder = Geocoder(this)
        try {
            // Only 1 address is needed here.
            val addresses = mGeocoder!!.getFromLocation(
                mLastLocation!!.latitude, mLastLocation!!.longitude, 1
            )
            if (addresses.size == 1) {
                val address = addresses[0]
                val addressLines = StringBuilder()
                //see here
                // https://stackoverflow.com/questions/44983507/android-getmaxaddresslineindex-returns-0-for-line-1
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
                placeAddress.text = addressLines.toString()
            } else {
                placeAddress.text = "WARNING! Geocoder returned more than 1 addresses!"
            }
            stopLocation()
        } catch (e: Exception) {
        }
    }

    override fun onRestart() {
        super.onRestart()
        goToBiometricActivity(this)
    }

    override fun onPause() {
        super.onPause()
        PostTemplate.setPlaceName(this, etPlaceName.text.toString())
        PostTemplate.setPlaceDesc(this, etPlaceDesc.text.toString())
    }

    override fun onResume() {
        super.onResume()
        etPlaceName.setText(PostTemplate.getPlaceName(this))
        etPlaceDesc.setText(PostTemplate.getPlaceDesc(this))
    }
}