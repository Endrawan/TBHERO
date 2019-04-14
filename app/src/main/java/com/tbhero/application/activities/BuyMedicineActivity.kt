package com.tbhero.application.activities

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import com.tbhero.application.R
import com.tbhero.application.R.layout.activity_buy_medicine
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_buy_medicine.*

class BuyMedicineActivity : AppCompatActivity() {

    private var medicineAlarm = Alarm()
    private lateinit var alarm: Alarm
    private lateinit var patient: User
    private var actStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_buy_medicine)

        initView()
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

    private fun initView() {
        // Initialize  Spinner
        val phaseAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            Alarm.CATEGORY_FASE_FASE
        )
        phaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        phase.getSpinner().adapter = phaseAdapter

        name.getEditText().isEnabled = false
        name.getEditText().isFocusable = false
        name.getEditText().isFocusableInTouchMode = false
        phase.getSpinner().isEnabled = false
    }

    private fun saveAlarm() {
        if (!verifForm()) return
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
    }

    private fun updateAlarm() {
        if (!verifForm()) return
        submit.showProgress()
        db.alarms.child(patient.id!!).child(medicineAlarm.id!!).setValue(medicineAlarm).addOnSuccessListener {
            toast("Berhasil Mengubah Data")
            submit.hideProgress()
            finish()
        }.addOnFailureListener {
            toast("Gagal Mengubah Alarm")
            submit.hideProgress()
        }
    }

    private fun verifForm(): Boolean {
        medicineAlarm.name = patient.name
        medicineAlarm.category = Alarm.CATEGORY_BELI_OBAT
        medicineAlarm.phaseCategory = phase.getSpinner().selectedItemPosition
        if (date.getTimeMills() != null) medicineAlarm.date = date.getTimeMills()
        medicineAlarm.dosage = quantity.getEditText().text.toString()
        medicineAlarm.desc = desc.getEditText().text.toString()

        if (medicineAlarm.date == null) {
            toast("Tolong masukkan tanggal pembelian!")
            return false
        }
        if (medicineAlarm.dosage!!.isEmpty()) {
            toast("Tolong masukkan jumlah obat!")
            return false
        }
        return true
    }

    private fun getExtras() {
        val alarmVal = intent.getStringExtra(Config.ARGS_ALARM)
        if (alarmVal != null) alarm = gson.fromJson(alarmVal, Alarm::class.java)
        patient = gson.fromJson(intent.getStringExtra(Config.ARGS_PATIENT), User::class.java)
        medicineAlarm = gson.fromJson(intent.getStringExtra(Config.ARGS_MEDICINE_ALARM), Alarm::class.java)
        actStatus = intent.getIntExtra(Config.ARGS_ACTIVITY_STATUS, Config.VALUE_ACTIVITY_STATUS_CREATE)

        name.getEditText().setText(patient.name)
        when (actStatus) {
            Config.VALUE_ACTIVITY_STATUS_CREATE -> {
                phase.getSpinner().setSelection(alarm.category!!)
            }
            Config.VALUE_ACTIVITY_STATUS_UPDATE -> {
                fillForm()
                submit.getButton().text = "Update"
            }
            Config.VALUE_ACTIVITY_STATUS_READ_ONLY -> {
                fillForm()
                date.getEditText().isEnabled = false
                quantity.getEditText().isEnabled = false
                quantity.getEditText().isFocusableInTouchMode = false
                desc.getEditText().isEnabled = false
                desc.getEditText().isFocusableInTouchMode = false
                submit.getButton().visibility = View.GONE
            }
        }
    }

    private fun fillForm() {
        date.setTimeMills(medicineAlarm.date)
        quantity.getEditText().setText(medicineAlarm.dosage)
        desc.getEditText().setText(medicineAlarm.desc)
        phase.getSpinner().setSelection(medicineAlarm.phaseCategory!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
