package com.example.mobileappproject

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.text.DateFormat
import java.util.*

class LocationActivity : AppCompatActivity() {

    // member views
    protected var mLatitudeText: TextView? = null
    protected var mLongitudeText: TextView? = null
    protected var mTimeText: TextView? = null
    protected var mOutput: TextView? = null
    protected var mLocateButton: Button? = null

    // member variables that hold location info
    protected var mLastLocation: Location? = null
    protected var mLocationRequest: LocationRequest? = null
    protected var mGeocoder: Geocoder? = null
    protected var mLocationProvider: FusedLocationProviderClient? = null

    companion object {
        var REQUEST_LOCATION = 1
    }

    var mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            mLastLocation = result.lastLocation
            mLatitudeText!!.text = mLastLocation!!.latitude.toString()
            mLongitudeText!!.text = mLastLocation!!.longitude.toString()
            mTimeText!!.text = DateFormat.getTimeInstance().format(Date())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // initialize views
        mLatitudeText = findViewById<View>(R.id.latitude_text) as TextView
        mLongitudeText = findViewById<View>(R.id.longitude_text) as TextView
        mTimeText = findViewById<View>(R.id.time_text) as TextView
        mLocateButton = findViewById<View>(R.id.locate) as Button
        mOutput = findViewById<View>(R.id.output) as TextView

        // below are placeholder values used when the app doesn't have the permission
        mLatitudeText?.text = "Latitude not available yet"
        mLongitudeText?.text = "Longitude not available yet"
        mTimeText?.text = "Time not available yet"
        mOutput?.text = ""

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            ActivityResultCallback<Map<String?, Boolean?>> { result: Map<String?, Boolean?> ->
                val fineLocationGranted = result.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION, false
                )
                val coarseLocationGranted = result.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION, false
                )
                if (fineLocationGranted != null && fineLocationGranted) {
                    // Precise location access granted.
                    // permissionOk = true;
                    mTimeText!!.text = "permission granted"
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    // Only approximate location access granted.
                    // permissionOk = true;
                    mTimeText!!.text = "permission granted"
                } else {
                    // permissionOk = false;
                    // No location access granted.
                    mTimeText!!.text = "permission not granted"
                }
            }
        )

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )


        // LocationReques sets how often etc the app receives location updates


        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10
        mLocationRequest!!.fastestInterval = 5
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun onStartClicked(v:View?) {
        mLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        mTimeText!!.text = "Started updating location"
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mLocationProvider!!.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallBack, Looper.getMainLooper()
        )
    }

    fun onLocateClicked(V:View?) {

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
                mOutput!!.text = addressLines
            } else {
                mOutput!!.text = "WARNING! Geocoder returned more than 1 addresses!"
            }
        } catch (e: Exception) {
        }
    }
}
