package com.example.mobileappproject

import android.content.Intent
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

        val isEmailChecked = checkEmail(editTextEmailAddress)
        val isPasswordChecked = checkPassword(editTextPassword)

        if (isEmailChecked && isPasswordChecked) {
            if (canUpgradeAnonymous(firebaseAuth)) {
                val credential = EmailAuthProvider.getCredential(email, password)
                firebaseAuth.currentUser?.linkWithCredential(credential)
            } else {
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
    }
    private fun canUpgradeAnonymous(firebaseAuth: FirebaseAuth): Boolean {
        return firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isAnonymous == true
    }

    private fun goToMain(){
//        val intent= Intent(this,MainActivity::class.java)
//        startActivity(intent)
        goToMainActivity(this)
    }

    private fun goToLogin(){
//        val intent= Intent(this,LoginActivity::class.java)
//        startActivity(intent)
        goToLoginActivity(this)
    }

    // if you do not add this check, then you would have to login everytime you start your application on your phone.
//    override fun onStart() {
//        super.onStart()
//        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
//            goToLogin()
//        }
//    }

}


