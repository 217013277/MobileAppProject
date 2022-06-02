package com.example.mobileappproject

import android.app.Activity
import android.content.Intent
import androidx.biometric.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.mobileappproject.extensions.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

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
        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
//        firebaseAuth = FirebaseAuth.getInstance()

        googleSignIn.setOnClickListener{
            Toast.makeText(this,"Logging In",Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

        //Biometric
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    goToMainActivity(this@LoginActivity)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }

            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricLoginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)

//            val isBiometricAuthSucceed = biometricAuth(this,this)
//            if (isBiometricAuthSucceed) {
//                Log.d("test biometric", "SUCCESS")
//            } else {
//                Log.d("test biometric", "FAIL")
//            }
        }
    }

    // Email and password login
    private fun emailSignIn() {
        val editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email = editTextEmailAddress.text.toString()
        val password = editTextPassword.text.toString()

        val isEmailChecked = checkEmail(editTextEmailAddress)
        val isPasswordChecked = checkPassword(editTextPassword)

        if (isEmailChecked && isPasswordChecked) {
            Firebase.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
//                    AccountPreference.setEmail(this, firebaseAuth.currentUser?.email.toString())
                    goToMainActivity(this)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
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

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                finish()
                goToMainActivity(this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val biometricBtn = findViewById<ImageButton>(R.id.biometricBtn)
        val tvAskIfAccount = findViewById<TextView>(R.id.tvAskIfAccountExisted)
        val goToRegisterBtn = findViewById<TextView>(R.id.tvToRegister)

        val user = Firebase.auth.currentUser

        if (user == null) {
            biometricBtn.visibility = View.GONE

            tvAskIfAccount.text = getString(R.string.do_not_have_an_account)
            goToRegisterBtn.setOnClickListener{
                goToRegisterActivity(this)
            }
        } else {
            tvAskIfAccount.text = getString(R.string.want_to_sign_in_with_another_account)
            goToRegisterBtn.text = getString(R.string.logout)
            goToRegisterBtn.setOnClickListener{
                Firebase.auth.signOut()
                goToRegisterActivity(this)
            }
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

