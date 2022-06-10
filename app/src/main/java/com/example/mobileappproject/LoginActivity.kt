package com.example.mobileappproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobileappproject.extensions.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val biometricLoginButton = findViewById<ImageButton>(R.id.biometricBtn)
        val googleSignIn = findViewById<SignInButton>(R.id.googleSignInBtn)

        loginBtn.setOnClickListener{
            emailSignIn()
        }

        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.app_web_client_id))
            .requestEmail()
            .build()
        // getting the value of gso inside the GoogleSignInClient
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        googleSignIn.setOnClickListener{
            Toast.makeText(this,"Logging In",Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

        //Biometric
        biometricLoginButton.setOnClickListener { openBiometricAuth() }
    }

    // Email and password login
    private fun emailSignIn() {
        val editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email = editTextEmailAddress.text.toString()
        val password = editTextPassword.text.toString()
        var (validEmail, validPassword) = checkValidForm(
            email,
            editTextEmailAddress,
            password,
            editTextPassword
        )

        if (validEmail && validPassword) {
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && Firebase.auth.currentUser != null) {
                        ActivityChanger().goToMainActivity(this)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun checkValidForm(
        email: String,
        editTextEmailAddress: EditText,
        password: String,
        editTextPassword: EditText
    ): Pair<Boolean, Boolean> {
        var validEmail = false
        var validPassword = false

        if (!FormValidator().checkIsNotEmpty(email)) {
            editTextEmailAddress.error = "This field is required"
        } else {
            if (!FormValidator().checkIsEmail(email)) {
                editTextEmailAddress.error = "The email is in bad format"
            } else {
                validEmail = true
            }
        }

        if (!FormValidator().checkIsNotEmpty(password)) {
            editTextPassword.error = "This field is required"
        } else {
            if (!FormValidator().checkIsPassword(password)) {
                editTextPassword.error = "Min length 6 and max length 12"
            } else {
                validPassword = true
            }
        }
        return Pair(validEmail, validPassword)
    }


    // Google login
    private fun signInGoogle(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
            Log.d("ApiException",e.toString())
        }
    }

    private fun openBiometricAuth() {
        val onSucceeded = Runnable {
            ActivityChanger().goToMainActivity(this)
        }
        BiometricAuth().biometricAuth(this, this, onSucceeded)
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                ActivityChanger().goToMainActivity(this)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val biometricBtn = findViewById<ImageButton>(R.id.biometricBtn)
        val tvLoggedInText = findViewById<TextView>(R.id.tvLoggedInText)
        val tvLoggedInEmail = findViewById<TextView>(R.id.tvLoggedInEmail)
        val tvAskIfAccount = findViewById<TextView>(R.id.tvAskIfAccountExisted)
        val goToRegisterBtn = findViewById<TextView>(R.id.tvToRegister)

        val user = Firebase.auth.currentUser

        goToRegisterBtn.setOnClickListener{
            ActivityChanger().goToRegisterActivity(this)
            finish()
            Toast.makeText(this,"go to Login Page",Toast.LENGTH_SHORT).show()
        }

        if (user == null) {
            biometricBtn.visibility = View.GONE
            tvLoggedInText.visibility = View.GONE
            tvLoggedInEmail.visibility = View.GONE
            tvAskIfAccount.text = getString(R.string.do_not_have_an_account)

        } else {
            tvLoggedInEmail.text = user.email
            tvAskIfAccount.text = getString(R.string.want_to_sign_in_with_another_account)
            goToRegisterBtn.text = getString(R.string.logout)
        }
    }

    // if you do not add this check, then you would have to login everytime you start your application on your phone.
//    override fun onStart() {
//        super.onStart()
//        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
//            goToMain()
//        }
//    }
}

