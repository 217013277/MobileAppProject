package com.example.mobileappproject

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproject.extensions.goToBiometricActivity
import com.example.mobileappproject.extensions.goToLoginActivity
import com.example.mobileappproject.lists.Place
import com.example.mobileappproject.lists.PlaceAdapter
import com.example.mobileappproject.lists.PlaceRowListener
import com.example.mobileappproject.lists.PlaceStatics
import com.example.mobileappproject.sharedPreferences.PostTemplate
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), PlaceRowListener {

    private lateinit var footer : RelativeLayout
    private lateinit var etPlaceName: EditText
    private lateinit var etPlaceDesc: EditText

    lateinit var _db: DatabaseReference
    var _placeList: MutableList<Place>? = null
    lateinit var _adapter: PlaceAdapter

    private var mLastLocation: Location? = null
    var mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            mLastLocation = result.lastLocation
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPlaceName = findViewById(R.id.etPlaceName)
        etPlaceDesc = findViewById(R.id.etPlaceDesc)

        _db = FirebaseDatabase.getInstance("https://vtclab-da73a-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        _placeList = mutableListOf()
//        _adapter = TaskAdapter(this, _taskList!!)
        _adapter = PlaceAdapter(this, _placeList!!)
        val recyclerview = findViewById<RecyclerView>(R.id.listviewTask)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = _adapter
        _adapter.setOnItemClickListener(object : PlaceAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@MainActivity,
                    "You clicked on item $position",
                    Toast.LENGTH_SHORT).show()
            }

        })

        val email = findViewById<TextView>(R.id.email)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val addBtn = findViewById<ImageButton>(R.id.btnAdd)

        val user = Firebase.auth.currentUser
        if (user != null) {
            email.text = user.email
        } else {
            email.text = getString(R.string.no_user_found)
            logoutBtn.visibility = View.GONE
        }

        val _taskListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                loadTaskList(dataSnapshot)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Item failed, log a message
                Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
            }
        }

        _db.orderByKey().addValueEventListener(_taskListener)

        addBtn.setOnClickListener{ addTask() }
        logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            goToLoginActivity(this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTaskList(dataSnapshot: DataSnapshot) {
        Log.d("MainActivity", "load Place list")
        val places = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (places.hasNext()) {
            _placeList!!.clear()
            val listIndex = places.next()
            val itemsIterator = listIndex.children.iterator()
            //check if the collection has any task or not
            while (itemsIterator.hasNext()) {
                //get current task
                val currentItem = itemsIterator.next()
                val place = Place.create()
                //get current data in a map
                val map = currentItem.value as HashMap<*, *>
                //key will return the Firebase ID
                place.objectId = currentItem.key
                place.placeName = map["placeName"] as String?
                place.placeDesc = map["placeDesc"] as String?
                place.isFav = map["isFav"] as Boolean?
                _placeList!!.add(place)
            }
        }
        //alert adapter that has changed
        _adapter.notifyDataSetChanged()
    }

    private fun addTask() {
        //Declare and Initialise the Task
        val place = Place.create()
        //Set Task Description and isDone Status
        place.placeName = etPlaceName.text.toString()
        place.placeDesc = etPlaceDesc.text.toString()
        place.isFav = false
        //Get the object id for the new task from the Firebase Database
        val newTask = _db.child(PlaceStatics.FIREBASE_TASK).push()
        place.objectId = newTask.key
        //Set the values for new task in the firebase using the footer form
        newTask.setValue(place)
        //Hide the footer and show the floating button
//        toggleFooter()
//        closeKeyboard(txtNewTaskDesc)
        //Reset the new task description field for reuse.
        etPlaceName.setText("")
        etPlaceDesc.setText("")
        Toast.makeText(this, "Task added to the list successfully" + place.objectId, Toast.LENGTH_SHORT).show()
    }

    override fun onFavClick(objectId: String, isFav: Boolean) {
        _db.child(PlaceStatics.FIREBASE_TASK).child(objectId).child("isFav").setValue(isFav).addOnCompleteListener{
            Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Cannot update", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPlaceDelete(objectId: String, placeName: String) {
        _db.child(PlaceStatics.FIREBASE_TASK).child(objectId).removeValue().addOnCompleteListener{
            Toast.makeText(this, "Removed $placeName ID: $objectId", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Cannot remove $placeName ID: $objectId", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        PostTemplate.setPlaceName(this, etPlaceName.text.toString())
        PostTemplate.setPlaceDesc(this, etPlaceDesc.text.toString())
        goToBiometricActivity(this)
    }

    override fun onResume() {
        super.onResume()
        etPlaceName.setText(PostTemplate.getPlaceName(this))
        etPlaceDesc.setText(PostTemplate.getPlaceDesc(this))
    }

//    private fun toggleFooter(){
//        if (footer.visibility == View.GONE) {
//            footer.visibility = View.VISIBLE
//            footerToggle.visibility = View.GONE
//        } else {
//            footer.visibility = View.GONE
//            footerToggle.visibility = View.VISIBLE
//        }
//    }
//
//    private fun closeKeyboard (view: View) {
//        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }
}