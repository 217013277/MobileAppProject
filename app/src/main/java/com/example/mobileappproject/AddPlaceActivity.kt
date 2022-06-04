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
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mobileappproject.extensions.goToBiometricActivity
import com.example.mobileappproject.extensions.goToMainActivity
import com.example.mobileappproject.lists.Place
import com.example.mobileappproject.lists.PlaceStatics
import com.example.mobileappproject.sharedPreferences.PostTemplate
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.util.*

class AddPlaceActivity : AppCompatActivity() {

    private lateinit var etPlaceName: EditText
    private lateinit var etPlaceDesc: EditText
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvPermission: TextView
    private lateinit var tvWeather: TextView

    private lateinit var _db: DatabaseReference

    // member variables that hold location info
    private var mLastLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationProvider: FusedLocationProviderClient? = null
    private var mGeocoder: Geocoder? = null

    private var mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            mLastLocation = result.lastLocation
            tvLatitude.text = mLastLocation!!.latitude.toString()
            tvLongitude.text = mLastLocation!!.longitude.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        etPlaceName = findViewById(R.id.etPlaceName)
        etPlaceDesc = findViewById(R.id.etPlaceDesc)
        tvLatitude = findViewById(R.id.latitudeValue)
        tvLongitude = findViewById(R.id.longitudeValue)
        tvAddress = findViewById(R.id.addressValue)
        tvTime = findViewById(R.id.tvTime)
        tvPermission = findViewById(R.id.tvPermission)
        tvWeather = findViewById(R.id.tvWeather)

        _db = FirebaseDatabase.getInstance("https://vtclab-da73a-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        findViewById<Button>(R.id.getLocationBtn).setOnClickListener {
            Toast.makeText(this, "Start to get location, please wait", Toast.LENGTH_LONG).show()
            getCurrentLocation()
        }
        findViewById<Button>(R.id.getAddressBtn).setOnClickListener { getAddress() }
        findViewById<Button>(R.id.getWeatherBtn).setOnClickListener { tvWeather() }
        findViewById<Button>(R.id.submitBtn).setOnClickListener { addPlace() }
        findViewById<Button>(R.id.goToMainBtn).setOnClickListener {
            goToMainActivity(this)
            finish()
        }

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String?, Boolean?> ->
            val fineLocationGranted = result.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION, false
            )
            val coarseLocationGranted = result.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION, false
            )
            if (fineLocationGranted != null && fineLocationGranted) {
                // Precise location access granted.
                // permissionOk = true;
                tvPermission.text = "permission granted"
            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                // Only approximate location access granted.
                // permissionOk = true;
                tvPermission.text = "permission granted"
            } else {
                // permissionOk = false;
                // No location access granted.
                tvPermission.text = "permission not granted"
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        // LocationRequest sets how often etc the app receives location updates
        mLocationRequest = LocationRequest
            .create()
            .setInterval(10)
            .setFastestInterval(5)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    private fun addPlace() {
        //Declare and Initialise the Task
        val place = Place.create()
        //Set Task Description and isDone Status
        place.placeName = etPlaceName.text.toString()
        place.placeDesc = etPlaceDesc.text.toString()
        place.placeLatitude = tvLatitude.text.toString()
        place.placeLongitude = tvLongitude.text.toString()
        place.placeAddress = tvAddress.text.toString()
        place.isFav = false
        //Get the object id for the new task from the Firebase Database
        val newTask = _db.child(PlaceStatics.FIREBASE_TASK).push()
        place.objectId = newTask.key
        //Set the values for new task in the firebase using the footer form
        newTask.setValue(place).addOnSuccessListener {
            etPlaceName.setText("")
            etPlaceDesc.setText("")
            tvLatitude.text = ""
            tvLongitude.text = ""
            tvAddress.text = ""
            Toast.makeText(this, "Task added to the list successfully" + place.objectId, Toast.LENGTH_SHORT).show()
            goToMainActivity(this)
        }.addOnFailureListener {
            Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show()
        }
        //Reset the new task description field for reuse.
    }

    private fun getCurrentLocation() {
        mLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        tvPermission.text = "Started updating location"
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

    private fun stopLocation() {
        val removeTask = mLocationProvider?.removeLocationUpdates(mLocationCallBack)
        removeTask?.addOnCompleteListener { task ->
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

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
//                getCurrentLocation()
//            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

    @SuppressLint("SetTextI18n")
    private fun getAddress() {
        mGeocoder = Geocoder(this)
        try {
            // Only 1 address is needed here.
            val addresses = mGeocoder!!.getFromLocation(
                mLastLocation!!.latitude, mLastLocation!!.longitude, 1
            )
            Log.d("getAddress", addresses.toString())
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
                Log.d("getAddress", "123123123123")
                tvAddress.text = addressLines.toString()
            } else {
                tvAddress.text = "WARNING! Geocoder returned more than 1 addresses!"
            }
        } catch (e: Exception) {
        }
    }

    private fun tvWeather() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${tvLatitude.text}&lon=${tvLongitude.text}&appid=${getString(R.string.open_weather_api_key)}"

        val weatherRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("Weather", "Response: $response")
                val weatherObject = JSONObject(response).getJSONArray("weather")[0]
                Log.d("Weather", weatherObject.toString())
                val weather = JSONObject(weatherObject.toString()).getString("main")
                Log.d("Weather", weather.toString())
                tvWeather.text = weather.toString()
            },
            { error ->
                Log.d("Weather", "error: $error")
            })

        queue.add(weatherRequest)
    }

    override fun onRestart() {
        super.onRestart()
        goToBiometricActivity(this)
    }

    override fun onPause() {
        super.onPause()
        PostTemplate.setPlaceName(this, etPlaceName.text.toString())
        PostTemplate.setPlaceDesc(this, etPlaceDesc.text.toString())
        PostTemplate.setLatitude(this, tvLatitude.text.toString())
        PostTemplate.setLongitude(this, tvLongitude.text.toString())
        PostTemplate.setAddress(this, tvAddress.text.toString())
    }

    override fun onResume() {
        super.onResume()
        tvTime.text = DateFormat.getDateTimeInstance().format(Date())

        etPlaceName.setText(PostTemplate.getPlaceName(this))
        etPlaceDesc.setText(PostTemplate.getPlaceDesc(this))
        tvLatitude.text = PostTemplate.getLatitude(this).toString()
        tvLongitude.text = PostTemplate.getLongitude(this).toString()
        tvAddress.text = PostTemplate.getAddress(this).toString()


        if (tvLatitude.text == "" || tvLongitude.text == "") {
            getCurrentLocation()
        }
    }
}