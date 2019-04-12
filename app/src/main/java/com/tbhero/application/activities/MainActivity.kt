package com.tbhero.application.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.tbhero.application.R
import com.tbhero.application.adapters.MainPagerAdapter
import com.tbhero.application.alarm.AlarmReceiver
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.fragments.ChatFragment
import com.tbhero.application.fragments.ProfileFragment
import com.tbhero.application.fragments.supervisi.ReminderFragment
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private val fragments = mutableListOf(ReminderFragment(), ChatFragment(), ProfileFragment())
    private val titles = mutableListOf("Reminder", "Chat", "Profile")
    private val icons = arrayOf(
        R.drawable.ic_reminder_white_24dp,
        R.drawable.ic_chat_white_24dp,
        R.drawable.ic_user_white_24dp
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (user.category) {
            User.USER_CATEGORY_PASIEN -> {
                setTheme(R.style.AppTheme_Pasien)
            }
            User.USER_CATEGORY_PMO -> {
                setTheme(R.style.AppTheme_PMO)
            }
        }
        setContentView(R.layout.activity_main)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout -> {
                logout()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createAlarm(value: Int) {
        val notificationIntent = Intent(this, AlarmReceiver::class.java)
        notificationIntent.putExtra(Config.ARGS_ALARM, gson.toJson(Alarm()))
        val broadcast = PendingIntent.getBroadcast(this, value, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, value)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, broadcast)
//        alarmManager.setInexactRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            1000 * 60,
//            broadcast
//        )
    }
}
