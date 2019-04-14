package com.tbhero.application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.tbhero.application.activities.MainActivity
import com.tbhero.application.activities.SignInActivity
import com.tbhero.application.components.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashScreenDuration()
        Handler().postDelayed({
            routeToAppropriatePage()
            finish()
        }, splashScreenDuration)
    }

    private fun getSplashScreenDuration() = 2000L

    private fun routeToAppropriatePage() {
        if (firebaseUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}
