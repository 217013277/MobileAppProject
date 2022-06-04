package com.example.mobileappproject.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun biometricAuth (fragmentActivity: FragmentActivity,
                   context: Context,
                   onSucceeded: Runnable? = null,
                   onFailed: Runnable? = null ,
                   onError: Runnable? = null,) {
    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(fragmentActivity,executor,
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(context,
                    "Authentication succeeded!", Toast.LENGTH_SHORT)
                    .show()
                onSucceeded?.run()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Authentication failed",
                    Toast.LENGTH_SHORT)
                    .show()
                onFailed?.run()
            }

            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context,
                    "Authentication error: $errString", Toast.LENGTH_SHORT)
                    .show()
                onError?.run()
            }
        })

    biometricPrompt.authenticate(promptInfo)
}

val promptInfo = BiometricPrompt.PromptInfo.Builder()
    .setTitle("Biometric login for my app")
    .setSubtitle("Log in using your biometric credential")
    .setNegativeButtonText("Cancel")
    .build()