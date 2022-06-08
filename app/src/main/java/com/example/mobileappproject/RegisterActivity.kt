package com.example.mobileappproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileappproject.extensions.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{
            register()
        }

        val goToLoginBtn = findViewById<TextView>(R.id.tvToLogin)
        goToLoginBtn.setOnClickListener{
            Toast.makeText(this,"go to Login Page",Toast.LENGTH_SHORT).show()
            ActivityChanger().goToLoginActivity(this)
        }

    }

    private fun register(){
        val editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email=editTextEmailAddress.text.toString()
        val password=editTextPassword.text.toString()

        val isEmailChecked = FormValidator().checkEmail(editTextEmailAddress)
        val isPasswordChecked = FormValidator().checkPassword(editTextPassword)

        if (isEmailChecked && isPasswordChecked) {
            Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    ActivityChanger().goToLoginActivity(this)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                Log.d("login exception", exception.toString())
            }
            Log.d("current_user", Firebase.auth.currentUser.toString())
        }
    }
    private fun canUpgradeAnonymous(firebaseAuth: FirebaseAuth): Boolean {
        return firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isAnonymous == true
    }

    // add this check, then you would have to login everytime you start your application on your phone.
    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser
        if(user!=null){
            ActivityChanger().goToBiometricActivity(this)
        }
    }
}


