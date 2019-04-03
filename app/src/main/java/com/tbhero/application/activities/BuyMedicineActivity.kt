package com.tbhero.application.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.tbhero.application.R
import com.tbhero.application.R.layout.activity_buy_medicine
import kotlinx.android.synthetic.main.activity_buy_medicine.*

class BuyMedicineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_buy_medicine)

        initToolbar()
        submit.setOnClickListener { finish() }
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
