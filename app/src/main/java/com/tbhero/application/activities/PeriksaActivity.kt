package com.tbhero.application.activities

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.tbhero.application.R
import com.tbhero.application.R.layout.activity_periksa
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_periksa.*

class PeriksaActivity : AppCompatActivity() {

    private lateinit var patient: User
    private var actStatus: Int = 0
    private var alarm = Alarm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_periksa)

        initToolbar()
        getExtras()

        submit.setOnClickListener {
            if (actStatus == Config.VALUE_ACTIVITY_STATUS_CREATE) saveAlarm()
            else if (actStatus == Config.VALUE_ACTIVITY_STATUS_UPDATE) updateAlarm()
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

    private fun saveAlarm() {
        verifForm()
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

    private fun updateAlarm() {
        verifForm()
        submit.showProgress()
        db.alarms.child(patient.id!!).child(alarm.id!!).setValue(alarm).addOnSuccessListener {
            toast("Berhasil Mengubah Data")
            submit.hideProgress()
            finish()
        }.addOnFailureListener {
            toast("Gagal Mengubah Alarm")
            submit.hideProgress()
        }
    }

    private fun getExtras() {
        patient = gson.fromJson(intent.getStringExtra(Config.ARGS_PATIENT), User::class.java)
        alarm = gson.fromJson(intent.getStringExtra(Config.ARGS_ALARM), Alarm::class.java)

        name.getEditText().setText(alarm.name)
        desc.getEditText().setText(alarm.desc)
        date.setTimeMills(alarm.date)
        name.getEditText().isEnabled = false
        name.getEditText().isFocusableInTouchMode = false
        name.getEditText().isFocusable = false

        actStatus = intent.getIntExtra(Config.ARGS_ACTIVITY_STATUS, Config.VALUE_ACTIVITY_STATUS_CREATE)
        when (actStatus) {
            Config.VALUE_ACTIVITY_STATUS_UPDATE -> {
                submit.getButton().text = "Update"
            }
            Config.VALUE_ACTIVITY_STATUS_READ_ONLY -> {
                desc.getEditText().isEnabled = false
                desc.getEditText().isFocusableInTouchMode = false
                desc.getEditText().isFocusable = false
                date.getEditText().isEnabled = false
                submit.getButton().visibility = View.GONE
            }
        }
    }

    private fun verifForm(): Boolean {
        alarm.category = Alarm.CATEGORY_PERIKSA
        alarm.desc = desc.getEditText().text.toString().trim()
        if (date.getTimeMills() != null) alarm.date = date.getTimeMills()
        alarm.name = patient.name
        return true
    }
}
