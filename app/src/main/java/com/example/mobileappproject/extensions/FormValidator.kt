package com.example.mobileappproject.extensions

import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class FormValidator {
    fun checkEmail(ediTextEmail: EditText): Boolean {
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

    fun checkPassword(editTextPassword: EditText): Boolean {
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

    fun checkIsNotEmpty(editText: EditText): Boolean {
        val et = editText.text.toString()
        if (et.isEmpty()) {
            editText.error = "This field is required"
            return false
        }
        return true
    }

}

