package com.example.mobileappproject.extensions

import android.content.Context
import android.content.Intent
import com.example.mobileappproject.BiometricActivity
import com.example.mobileappproject.LoginActivity

import com.example.mobileappproject.MainActivity
import com.example.mobileappproject.RegisterActivity

fun goToMainActivity(context: Context){
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

fun goToRegisterActivity(context: Context){
    val intent = Intent(context, RegisterActivity::class.java)
    context.startActivity(intent)
}

fun goToLoginActivity(context: Context){
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
}

fun goToBiometricActivity(context: Context){
    val intent = Intent(context, BiometricActivity::class.java)
    context.startActivity(intent)
}