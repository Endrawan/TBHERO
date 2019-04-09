package com.tbhero.application.activities

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import com.tbhero.application.R
import com.tbhero.application.R.layout.activity_buy_medicine
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_buy_medicine.*

class BuyMedicineActivity : AppCompatActivity() {

    private val medicineAlarm = Alarm()
    private lateinit var alarm: Alarm
    private lateinit var patient: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_buy_medicine)

        alarm = gson.fromJson(intent.getStringExtra(Config.ARGS_ALARM), Alarm::class.java)
        patient = gson.fromJson(intent.getStringExtra(Config.ARGS_PATIENT), User::class.java)

        initView()
        initToolbar()
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
        // Initialize  Spinner
        val phaseAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            Alarm.CATEGORY_FASE_FASE
        )
        phaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        phase.getSpinner().adapter = phaseAdapter
        phase.getSpinner().setSelection(alarm.category!!)

        name.getEditText().setText(patient.name)
    }

    private fun saveAlarm() {
        medicineAlarm.name = patient.name
        medicineAlarm.category = Alarm.CATEGORY_BELI_OBAT
        medicineAlarm.phaseCategory = phase.getSpinner().selectedItemPosition
        medicineAlarm.date = date.getTimeMills()
        medicineAlarm.dosage = quantity.getEditText().text.toString()
        medicineAlarm.desc = desc.getEditText().text.toString()

        submit.showProgress()
        val medicineRef = db.alarms.child(patient.id!!).push()
        medicineAlarm.id = medicineRef.key
        medicineRef.setValue(medicineAlarm).addOnSuccessListener {
            val alarmRef = db.alarms.child(patient.id!!).push()
            alarm.id = alarmRef.key
            alarmRef.setValue(alarm).addOnSuccessListener {
                toast("Berhasil menambah alarm!")
                submit.hideProgress()
                setResult(Activity.RESULT_OK)
                finish()
            }.addOnFailureListener {
                toast("Gagal menambah alarm, " + it.message)
                submit.hideProgress()
            }
        }.addOnFailureListener {
            toast("Gagal menambah alarm beli obat, " + it.message)
            submit.hideProgress()
        }

        name.getEditText().isEnabled = false
        phase.getSpinner().isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
