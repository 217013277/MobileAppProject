package com.example.mobileappproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mobileappproject.extensions.checkEmail
import com.example.mobileappproject.extensions.checkPassword
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var  firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{
            register()
        }

        val goToLoginBtn = findViewById<TextView>(R.id.textViewToLogin)
        goToLoginBtn.setOnClickListener{
            Toast.makeText(this,"go to Register Page",Toast.LENGTH_SHORT).show()
            goToLogin()
        }

        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun register(){
        val editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email=editTextEmailAddress.text.toString()
        val password=editTextPassword.text.toString()

        var isEmailChecked = checkEmail(this, editTextEmailAddress)
        var isPasswordChecked = checkPassword(this, editTextPassword)

        if (isEmailChecked && isPasswordChecked) {
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
//                firebaseAuth.currentUser
                    finish()
                    goToMain()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                Log.d("login exception", exception.toString())
            }
        }
    }

    private fun goToMain(){
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogin(){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    // if you do not add this check, then you would have to login everytime you start your application on your phone.
//    override fun onStart() {
//        super.onStart()
//        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
//            goToLogin()
//        }
//    }
}


