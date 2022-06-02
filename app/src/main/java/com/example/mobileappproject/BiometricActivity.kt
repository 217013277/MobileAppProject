package com.example.mobileappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.mobileappproject.extensions.biometricAuth
import com.example.mobileappproject.extensions.goToLoginActivity
import com.example.mobileappproject.extensions.goToMainActivity
import java.util.concurrent.Executor

class BiometricActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)

        openBiometricAuth()

        val biometricLoginButton = findViewById<ImageButton>(R.id.biometricBtn)
        val backToLoginBtn = findViewById<TextView>(R.id.backToLoginBtn)

        biometricLoginButton.setOnClickListener { openBiometricAuth() }
        backToLoginBtn.setOnClickListener { goToLoginActivity(this) }
    }

    private fun openBiometricAuth() {
        val onSucceeded = Runnable {
            goToMainActivity(this)
        }
        biometricAuth(this, this, onSucceeded)
    }
}