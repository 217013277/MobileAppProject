package com.example.mobileappproject.extensions

import androidx.core.util.PatternsCompat

class FormValidator {
    fun checkIsEmail(email: String): Boolean {
//        val email = ediTextEmail.text.toString()
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
//            ediTextEmail.error = "The email is in bad format"
            return false
        }
        return true
    }

    fun checkIsPassword(password: String): Boolean {
//        val password = editTextPassword.text.toString()

//        if (password.isEmpty()) {
//            editTextPassword.error = "This field is required"
//            return false
//        }
        if (password.length < 6 || password.length > 12) {
//            editTextPassword.error = "Min length 6 and max length 12"
            return false
        }
        return true
    }

    fun checkIsNotEmpty(string: String): Boolean {
//        val et = editText.text.toString()
        if (string.isEmpty()) {
//            editText.error = "This field is required"
            return false
        }
        return true
    }

}

