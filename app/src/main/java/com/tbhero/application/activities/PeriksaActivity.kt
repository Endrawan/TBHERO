package com.tbhero.application.activities

import android.os.Bundle
import android.view.Menu
import com.tbhero.application.R
import com.tbhero.application.R.layout.activity_periksa
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_periksa.*

class PeriksaActivity : AppCompatActivity() {

    private lateinit var patient: User
    private val alarm = Alarm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_periksa)

        patient = gson.fromJson(intent.getStringExtra(Config.ARGS_PATIENT), User::class.java)
        initToolbar()
        initView()

        submit.setOnClickListener {
            saveAlarm()
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

    private fun initView() {
        name.getEditText().setText(patient.name)
        name.getEditText().isEnabled = false
        name.getEditText().isFocusableInTouchMode = false
        name.getEditText().isFocusable = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun saveAlarm() {
        alarm.category = Alarm.CATEGORY_PERIKSA
        alarm.desc = desc.getEditText().text.toString().trim()
        alarm.date = date.getTimeMills()
        alarm.name = patient.name

        submit.showProgress()
        val alarmRef = db.alarms.child(patient.id!!).push()
        alarm.id = alarmRef.key
        alarmRef.setValue(alarm).addOnSuccessListener {
            submit.hideProgress()
            toast("Berhasil menambah alarm!")
            finish()
        }.addOnFailureListener {
            submit.hideProgress()
            toast("Gagal menambah alarm! " + it.message)
        }
    }
}
