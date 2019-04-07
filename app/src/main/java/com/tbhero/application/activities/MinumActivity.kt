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
    private val alarm = Alarm()
    private val repeatViews: MutableList<CheckBox> = mutableListOf()
    lateinit var patient: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_minum)

        patient = gson.fromJson(intent.getStringExtra(Config.ARGS_PATIENT), User::class.java)

        initToolbar()
        initView()

        submit.setOnClickListener {
            saveAlarm()
            val i = Intent(this, BuyMedicineActivity::class.java)
            i.putExtra(Config.ARGS_ALARM, gson.toJson(alarm))
            i.putExtra(Config.ARGS_PATIENT, intent.getStringExtra(Config.ARGS_PATIENT))
            startActivityForResult(i, RC_MEDICINE)
//            toast(gson.toJson(alarm))
//            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
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

    private fun saveAlarm() {
        alarm.category = category.getSpinner().selectedItemPosition
        alarm.desc = desc.getEditText().text.toString().trim()
        alarm.dosage = dosage.getEditText().text.toString().trim()
        alarm.name = patient.name

        if (alarm.category == Alarm.CATEGORY_FASE_LANJUTAN) {
            if (Build.VERSION.SDK_INT < 23)
                alarm.time = time.currentHour.toString() + ":" + time.currentMinute.toString()
            else
                alarm.time = time.hour.toString() + ":" + time.minute.toString()

            alarm.repeat = getRepeatValue()

        } else {
            alarm.time = "5:00"
        }
    }

    private fun getRepeatValue(): MutableList<Boolean> {
        val result = mutableListOf<Boolean>()
        for (i in repeatViews) result.add(i.isChecked)
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_MEDICINE && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }
}
