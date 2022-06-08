package com.example.mobileappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.example.mobileappproject.extensions.ActivityChanger
import com.example.mobileappproject.extensions.BiometricAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class BiometricActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)

        openBiometricAuth()

        val biometricLoginButton = findViewById<ImageButton>(R.id.biometricBtn)
        val backToLoginBtn = findViewById<TextView>(R.id.backToLoginBtn)

        biometricLoginButton.setOnClickListener { openBiometricAuth() }
        backToLoginBtn.setOnClickListener {
            Firebase.auth.signOut()
            finish()
            ActivityChanger().goToLoginActivity(this)
        }
    }

    private fun openBiometricAuth() {
        val onSucceeded = Runnable {
            ActivityChanger().goToMainActivity(this)
            finish()
        }
        BiometricAuth().biometricAuth(this, this, onSucceeded)
    }
}
