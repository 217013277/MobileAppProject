package com.example.mobileappproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {

    private lateinit var  gso: GoogleSignInOptions
    private lateinit var  gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = findViewById<TextView>(R.id.email)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val email2 = findViewById<TextView>(R.id.email2)

        logoutBtn.setOnClickListener{
            logout()
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this,gso)

        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        if(googleAccount != null) {
            val personEmail = googleAccount.email

            email.text = personEmail
        }

        email2.text = AccountPreference.getEmail(this).toString()
    }

    private fun logout() {
        gsc.signOut().addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                goToLogin()
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun goToLogin(){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}