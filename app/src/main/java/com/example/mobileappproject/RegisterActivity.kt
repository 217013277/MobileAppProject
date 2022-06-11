package com.example.mobileappproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileappproject.extensions.*
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
        val (validEmail, validPassword) = checkValidForm(
            email,
            editTextEmailAddress,
            password,
            editTextPassword
        )

        if (validEmail && validPassword) {
            Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    ActivityChanger().goToLoginActivity(this)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                Log.d("login exception", exception.toString())
            }
        }
    }

    private fun checkValidForm(
        email: String,
        editTextEmailAddress: EditText,
        password: String,
        editTextPassword: EditText
    ): Pair<Boolean, Boolean> {
        var validEmail = false
        var validPassword = false

        if (!FormValidator().checkIsNotEmpty(email)) {
            editTextEmailAddress.error = "This field is required"
        } else {
            if (!FormValidator().checkIsEmail(email)) {
                editTextEmailAddress.error = "The email is in bad format"
            } else {
                validEmail = true
            }
        }

        if (!FormValidator().checkIsNotEmpty(password)) {
            editTextPassword.error = "This field is required"
        } else {
            if (!FormValidator().checkIsPassword(password)) {
                editTextPassword.error = "Min length 6 and max length 12"
            } else {
                validPassword = true
            }
        }
        return Pair(validEmail, validPassword)
    }

//    private fun canUpgradeAnonymous(firebaseAuth: FirebaseAuth): Boolean {
//        return firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isAnonymous == true
//    }

    // add this check, then you would have to login everytime you start your application on your phone.
    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser
        if(user!=null){
            ActivityChanger().goToBiometricActivity(this)
        }
    }
}


