package com.example.mobileappproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproject.extensions.goToAddPlaceActivity
import com.example.mobileappproject.extensions.goToBiometricActivity
import com.example.mobileappproject.extensions.goToLoginActivity
import com.example.mobileappproject.lists.Place
import com.example.mobileappproject.lists.PlaceAdapter
import com.example.mobileappproject.lists.PlaceRowListener
import com.example.mobileappproject.lists.PlaceStatics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), PlaceRowListener {

    lateinit var _db: DatabaseReference
    var _placeList: MutableList<Place>? = null
    lateinit var _adapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)

        _db = FirebaseDatabase.getInstance(getString(R.string.firebase_realtime_database_url)).reference
        _placeList = mutableListOf()
        _adapter = PlaceAdapter(this, _placeList!!)
        val recyclerview = findViewById<RecyclerView>(R.id.listviewTask)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = _adapter

        val email = findViewById<TextView>(R.id.email)
        val accountBtn = findViewById<Button>(R.id.AccountBtn)
        val user = Firebase.auth.currentUser
        if (user != null) {
            email.text = user.email
        } else {
            email.text = getString(R.string.no_user_found)
            accountBtn.visibility = View.GONE
        }
        accountBtn.setOnClickListener {
            Firebase.auth.signOut()
            goToLoginActivity(this)
            finish()
        }

        val _taskListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                loadPlaceList(dataSnapshot)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Item failed, log a message
                Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
            }
        }

        _db.orderByKey().addValueEventListener(_taskListener)

        findViewById<Button>(R.id.goToAddPlaceBtn).setOnClickListener{ goToAddPlaceActivity(this) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPlaceList(dataSnapshot: DataSnapshot) {
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
                place.placeAddress = map["placeAddress"] as String?
                place.isFav = map["isFav"] as Boolean?
                _placeList!!.add(place)
                place.imageUrl = map["imageUrl"] as String?
            }
        }
        //alert adapter that has changed
        _adapter.notifyDataSetChanged()
    }

    override fun onFavClick(objectId: String, isFav: Boolean) {
        _db.child(PlaceStatics.FIREBASE_PLACE).child(objectId).child("isFav").setValue(isFav).addOnCompleteListener{
            Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Cannot update", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPlaceDelete(objectId: String, placeName: String) {
        _db.child(PlaceStatics.FIREBASE_PLACE).child(objectId).removeValue().addOnCompleteListener{
            Toast.makeText(this, "Removed $placeName ID: $objectId", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Cannot remove $placeName ID: $objectId", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemSelect(objectId: String) {
        Toast.makeText(this@MainActivity, "You clicked on item $objectId", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, DetailPlaceActivity::class.java);
                val b = Bundle()
                b.putString("objectId", objectId)
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish()
    }

//    override fun onRestart() {
//        super.onRestart()
//        goToBiometricActivity(this)
//    }


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