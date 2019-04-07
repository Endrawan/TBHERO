package com.tbhero.application

import android.content.Intent
import android.os.Bundle
import com.tbhero.application.activities.MainActivity
import com.tbhero.application.activities.SignInActivity
import com.tbhero.application.components.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (firebaseUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        finish()
    }
}
