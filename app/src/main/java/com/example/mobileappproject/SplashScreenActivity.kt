package com.example.mobileappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.mobileappproject.extensions.goToMainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
            val splashScreen = findViewById<LinearLayout>(R.id.splashLauncher)
        splashScreen.alpha = 0f
            splashScreen.animate().setDuration(3000).alpha(1f).withEndAction {

                goToMainActivity(this)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
    }
}