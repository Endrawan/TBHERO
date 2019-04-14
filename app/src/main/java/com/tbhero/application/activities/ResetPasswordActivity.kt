package com.tbhero.application.activities

import android.os.Bundle
import android.view.Menu
import com.tbhero.application.R
import com.tbhero.application.components.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        initToolbar()

        reset.setOnClickListener {
            reset.showProgress()
            auth.sendPasswordResetEmail(email.getEditText().text.toString().trim())
                .addOnSuccessListener {
                    toast("Konfirmasi reset email sudah dikirim ke email anda.")
                    reset.hideProgress()
                }.addOnFailureListener {
                    toast("Gagal mengirim konfirmasi reset email! ${it.message}")
                    reset.hideProgress()
                }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val sab = supportActionBar
        sab?.setDisplayHomeAsUpEnabled(true)
        sab?.setDisplayShowHomeEnabled(true)
        sab?.setHomeAsUpIndicator(R.drawable.ic_close_grey_20dp)
        sab?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
