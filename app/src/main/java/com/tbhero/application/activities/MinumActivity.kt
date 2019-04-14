package com.tbhero.application.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import com.tbhero.application.R
import com.tbhero.application.R.layout.activity_minum
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_minum.*


class MinumActivity : AppCompatActivity() {

    private val RC_MEDICINE = 100
    private var alarm = Alarm()
    private var actStatus: Int = 0
    private val repeatViews: MutableList<CheckBox> = mutableListOf()
    lateinit var patient: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_minum)

        patient = gson.fromJson(intent.getStringExtra(Config.ARGS_PATIENT), User::class.java)

        initToolbar()
        initView()
        getExtras()

        submit.setOnClickListener {
            if (actStatus == Config.VALUE_ACTIVITY_STATUS_CREATE) {
                if (verifForm()) {
                    val i = Intent(this, BuyMedicineActivity::class.java)
                    i.putExtra(Config.ARGS_MEDICINE_ALARM, gson.toJson(Alarm()))
                    i.putExtra(Config.ARGS_ALARM, gson.toJson(alarm))
                    i.putExtra(Config.ARGS_PATIENT, intent.getStringExtra(Config.ARGS_PATIENT))
                    i.putExtra(Config.ARGS_ACTIVITY_STATUS, Config.VALUE_ACTIVITY_STATUS_CREATE)
                    startActivityForResult(i, RC_MEDICINE)
                }
            } else if (actStatus == Config.VALUE_ACTIVITY_STATUS_UPDATE) {
                updateAlarm()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_MEDICINE && resultCode == Activity.RESULT_OK) {
            finish()
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
        val categoryAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            Alarm.CATEGORY_FASE_FASE
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category.getSpinner().adapter = categoryAdapter
        category.getSpinner().onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                when (position) {
                    Alarm.CATEGORY_FASE_AWAL -> repeatOptions.visibility = View.GONE
                    Alarm.CATEGORY_FASE_LANJUTAN -> repeatOptions.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }

        // Initialize TimePicker
        time.setIs24HourView(true)

        // Initialize Repeat View
        repeatViews.add(monday)
        repeatViews.add(tuesday)
        repeatViews.add(wednesday)
        repeatViews.add(thursday)
        repeatViews.add(friday)
        repeatViews.add(saturday)
        repeatViews.add(sunday)

        name.getEditText().setText(patient.name)
        name.getEditText().isEnabled = false
    }

    private fun getRepeatValue(): MutableList<Boolean> {
        val result = mutableListOf<Boolean>()
        for (i in repeatViews) result.add(i.isChecked)
        return result
    }

    private fun setRepeatValue() {
        for (i in repeatViews.indices)
            repeatViews[i].isChecked = alarm.repeat[i]
    }

    private fun clearRepeat() {
        for (i in repeatViews) i.isChecked = false
    }

    private fun getExtras() {
        patient = gson.fromJson(intent.getStringExtra(Config.ARGS_PATIENT), User::class.java)
        alarm = gson.fromJson(intent.getStringExtra(Config.ARGS_ALARM), Alarm::class.java)
        actStatus = intent.getIntExtra(Config.ARGS_ACTIVITY_STATUS, Config.VALUE_ACTIVITY_STATUS_CREATE)
        when (actStatus) {
            Config.VALUE_ACTIVITY_STATUS_UPDATE -> {
                fillForm()
                submit.getButton().text = "Update"
            }
            Config.VALUE_ACTIVITY_STATUS_READ_ONLY -> {
                fillForm()
                category.getSpinner().isEnabled = false
                time.isEnabled = false
                submit.getButton().visibility = View.GONE
                dosage.getEditText().isEnabled = false
                desc.getEditText().isEnabled = false
            }
        }
    }

    private fun fillForm() {
        category.getSpinner().setSelection(alarm.category!!)
        dosage.getEditText().setText(alarm.dosage)
        if (Build.VERSION.SDK_INT < 23) {
            time.currentHour = alarm.hour
            time.currentMinute = alarm.minute
        } else {
            time.hour = alarm.hour
            time.minute = alarm.minute
        }
        desc.getEditText().setText(alarm.desc)
        setRepeatValue()
    }

    private fun updateAlarm() {
        if (!verifForm()) return
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

    private fun verifForm(): Boolean {
        alarm.category = category.getSpinner().selectedItemPosition
        alarm.desc = desc.getEditText().text.toString().trim()
        alarm.dosage = dosage.getEditText().text.toString().trim()
        alarm.name = patient.name

        if (Build.VERSION.SDK_INT < 23) {
            alarm.hour = time.currentHour
            alarm.minute = time.currentMinute
        } else {
            alarm.hour = time.hour
            alarm.minute = time.minute
        }

        if (alarm.category == Alarm.CATEGORY_FASE_LANJUTAN) {
            alarm.repeat = getRepeatValue()
        }

        if (alarm.dosage!!.isEmpty()) {
            toast("Tolong masukkan dosis obat!")
            return false
        }
        return true
    }
}

