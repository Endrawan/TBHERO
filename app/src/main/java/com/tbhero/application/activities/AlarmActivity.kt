package com.tbhero.application.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.tbhero.application.R
import com.tbhero.application.R.layout.activity_alarm
import com.tbhero.application.adapters.MainPagerAdapter
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.fragments.AlarmsFragment
import com.tbhero.application.fragments.ContactsFragment
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity : AppCompatActivity() {

    private val fragments = mutableListOf(AlarmsFragment(), ContactsFragment())
    private val titles = mutableListOf("Reminder", "Profile")
    private val icons = arrayOf(
        R.drawable.ic_reminder_white_24dp,
        R.drawable.ic_user_white_24dp
    )
    lateinit var patient: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_alarm)

        patient = gson.fromJson(intent.getStringExtra(Config.ARGS_PATIENT), User::class.java)

        initToolbar()
        initTabLayout()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val sab = supportActionBar
        sab?.setDisplayShowTitleEnabled(true)
    }

    private fun initTabLayout() {
        val adapter = MainPagerAdapter(supportFragmentManager, fragments, titles)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        for (i in fragments.indices) {
            val tab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
            tab.text = titles[i]
            tab.setCompoundDrawablesWithIntrinsicBounds(0, icons[i], 0, 0)
            tabLayout.getTabAt(i)?.customView = tab
        }
    }
}
