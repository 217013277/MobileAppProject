package com.example.mobileappproject.extensions

import android.content.Context
import android.util.Patterns
import android.widget.EditText

fun checkEmail(context: Context, ediTextEmail: EditText): Boolean {
    val email = ediTextEmail.text.toString()

    if (email.isEmpty()) {
        ediTextEmail.error = "This field is required"
        return false
    }
    if (Patterns.EMAIL_ADDRESS.matcher(email).matches() == false) {
        ediTextEmail.error = "The email is in bad format"
        return false
    }
    return true
}

fun checkPassword(context: Context, editTextPassword: EditText): Boolean {
    val password = editTextPassword.text.toString()

    if (password.isEmpty()) {
        editTextPassword.error = "This field is required"
        return false
    }
    if (password.length < 6) {
        editTextPassword.error = "Min length 6"
        return false
    }
    if (password.length > 12) {
        editTextPassword.error = "Max length 12"
        return false
    }
    return true
}

