package com.example.mobileappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mobileappproject.extensions.goToLoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = findViewById<TextView>(R.id.email)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)

        val user = Firebase.auth.currentUser
        if (user != null) {
            email.text = user.displayName
        } else {
            email.text = getString(R.string.no_user_found)
        }

        logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            goToLogin()
        }
    }

//    private fun logout() {
//        gsc.signOut().addOnCompleteListener(this) { task ->
//            if(task.isSuccessful){
//                goToLogin()
//                finish()
//            }
//        }.addOnFailureListener { exception ->
//            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
//        }
//    }

    private fun goToLogin(){
        goToLoginActivity(this)
    }
}