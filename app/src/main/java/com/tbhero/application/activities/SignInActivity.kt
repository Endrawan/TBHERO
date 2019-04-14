package com.tbhero.application.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.tbhero.application.R
import com.tbhero.application.R.drawable.ic_close_grey_20dp
import com.tbhero.application.R.layout.activity_sign_in
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.components.Extension
import com.tbhero.application.models.Config
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private val TAG = "SignInActivity"
    private val RC_SIGN_UP = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_sign_in)
        initToolbar()

        signUp.setOnClickListener {
            startActivityForResult(Intent(this, SignUpActivity::class.java), RC_SIGN_UP)
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        signIn.setOnClickListener {
            signIn()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val sab = supportActionBar
        sab?.setDisplayHomeAsUpEnabled(true)
        sab?.setDisplayShowHomeEnabled(true)
        sab?.setHomeAsUpIndicator(ic_close_grey_20dp)
        sab?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_UP -> {
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun signIn() {
        val emailVal = email.getEditText().text.toString().trim()
        val passwordVal = password.getEditText().text.toString().trim()

        if (!Extension.isEmailValid(emailVal)) {
            toast("Email yang anda masukkan belum benar")
            return
        }

        if (passwordVal.length < Config.MIN_LENGTH_PASSWORD) {
            toast("Password minimal ${Config.MIN_LENGTH_PASSWORD} Karakter!")
            return
        }

        signIn.showProgress()
        auth.signInWithEmailAndPassword(emailVal, passwordVal)
            .addOnSuccessListener {
                Log.d(TAG, "signInWithEmail:success")
                updateUser({
                    signIn.hideProgress()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, { signIn.hideProgress() })
            }.addOnFailureListener {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", it)
                toast("Gagal melakukan sign in! ${it.message}")
                signIn.hideProgress()
            }
    }
}
