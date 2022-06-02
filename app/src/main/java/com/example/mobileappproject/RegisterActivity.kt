package com.example.mobileappproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileappproject.extensions.checkEmail
import com.example.mobileappproject.extensions.checkPassword
import com.example.mobileappproject.extensions.goToLoginActivity
import com.example.mobileappproject.extensions.goToMainActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

//    private lateinit var  firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{
            register()
        }

        val goToLoginBtn = findViewById<TextView>(R.id.tvToLogin)
        goToLoginBtn.setOnClickListener{
            Toast.makeText(this,"go to Register Page",Toast.LENGTH_SHORT).show()
            goToLoginActivity(this)
        }

//        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun register(){
        val editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email=editTextEmailAddress.text.toString()
        val password=editTextPassword.text.toString()

        val isEmailChecked = checkEmail(editTextEmailAddress)
        val isPasswordChecked = checkPassword(editTextPassword)

        if (isEmailChecked && isPasswordChecked) {
            Log.d("current_user", Firebase.auth.currentUser.toString())
            if (canUpgradeAnonymous(Firebase.auth)) {
                val credential = EmailAuthProvider.getCredential(email, password)
                Firebase.auth.currentUser?.linkWithCredential(credential)
            } else {
                Firebase.auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
//                firebaseAuth.currentUser
                        finish()
                        goToMainActivity(this)
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    Log.d("login exception", exception.toString())
                }
            }
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
            goToLoginActivity(this)
        }
    }
}


