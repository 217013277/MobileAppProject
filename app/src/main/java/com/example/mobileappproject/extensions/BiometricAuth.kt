package com.example.mobileappproject.extensions

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun biometricAuth (fragmentActivity: FragmentActivity, context: Context): Boolean {
    var isSucceed = true

    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(fragmentActivity,executor,
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context,
                    "Authentication error: $errString", Toast.LENGTH_SHORT)
                    .show()
                isSucceed = false
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(context,
                    "Authentication succeeded!", Toast.LENGTH_SHORT)
                    .show()
                isSucceed = true
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Authentication failed",
                    Toast.LENGTH_SHORT)
                    .show()
                isSucceed = false
            }
        })

    biometricPrompt.authenticate(promptInfo)

    return isSucceed
}

val promptInfo = BiometricPrompt.PromptInfo.Builder()
    .setTitle("Biometric login for my app")
    .setSubtitle("Log in using your biometric credential")
    .setNegativeButtonText("Use account password")
    .build()