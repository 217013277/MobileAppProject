package com.example.mobileappproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproject.extensions.goToLoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), TaskRowListener {

    private lateinit var footerToggle : Button
    private lateinit var footer : RelativeLayout
    private lateinit var txtNewTaskDesc: EditText

    lateinit var _db: DatabaseReference
    var _taskList: MutableList<Task>? = null
//    lateinit var _adapter: TaskAdapter
    lateinit var _adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        footer = findViewById(R.id.footer)
        txtNewTaskDesc = findViewById(R.id.txtNewTaskDesc)

        _db = FirebaseDatabase.getInstance("https://vtclab-da73a-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        _taskList = mutableListOf()
//        _adapter = TaskAdapter(this, _taskList!!)
        _adapter = RecyclerViewAdapter(this, _taskList!!)
        val recyclerview = findViewById<RecyclerView>(R.id.listviewTask)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = _adapter
        _adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener{
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
            goToLogin()
        }
    }

    private fun loadTaskList(dataSnapshot: DataSnapshot) {
        Log.d("MainActivity", "loadTaskList")
        val tasks = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (tasks.hasNext()) {
            _taskList!!.clear()
            val listIndex = tasks.next()
            val itemsIterator = listIndex.children.iterator()
            //check if the collection has any task or not
            while (itemsIterator.hasNext()) {
                //get current task
                val currentItem = itemsIterator.next()
                val task = Task.create()
                //get current data in a map
                val map = currentItem.value as HashMap<*, *>
                //key will return the Firebase ID
                task.objectId = currentItem.key
                task.done = map["done"] as Boolean
                task.taskDesc = map["taskDesc"] as String?
                _taskList!!.add(task)
            }
        }
        //alert adapter that has changed
        _adapter.notifyDataSetChanged()
    }

    private fun addTask() {
        //Declare and Initialise the Task
        val task = Task.create()
        //Set Task Description and isDone Status
        task.taskDesc = txtNewTaskDesc.text.toString()
        task.done = false
        //Get the object id for the new task from the Firebase Database
        val newTask = _db.child(Statics.FIREBASE_TASK).push()
        task.objectId = newTask.key
        //Set the values for new task in the firebase using the footer form
        newTask.setValue(task)
        //Hide the footer and show the floating button
//        toggleFooter()
//        closeKeyboard(txtNewTaskDesc)
        //Reset the new task description field for reuse.
        txtNewTaskDesc.setText("")
        Toast.makeText(this, "Task added to the list successfully" + task.objectId, Toast.LENGTH_SHORT).show()
    }

    override fun onTaskChange(objectId: String, isDone: Boolean) {
        _db.child(Statics.FIREBASE_TASK).child(objectId).child("done").setValue(isDone).addOnCompleteListener{
            Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Cannot update", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTaskDelete(objectId: String, desc: String) {
        _db.child(Statics.FIREBASE_TASK).child(objectId).removeValue().addOnCompleteListener{
            Toast.makeText(this, "Removed $desc ID: $objectId", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Cannot remove $desc ID: $objectId", Toast.LENGTH_SHORT).show()
        }
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

    private fun goToLogin(){
        goToLoginActivity(this)
    }
}