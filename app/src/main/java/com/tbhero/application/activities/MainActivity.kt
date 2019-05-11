package com.tbhero.application.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.tbhero.application.R
import com.tbhero.application.activities.profile_activities.PMOProfileActivity
import com.tbhero.application.activities.profile_activities.PasienProfileActivity
import com.tbhero.application.activities.profile_activities.SupervisiProfileActivity
import com.tbhero.application.adapters.MainPagerAdapter
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.fragments.ChatFragment
import com.tbhero.application.fragments.ContactsFragment
import com.tbhero.application.fragments.supervisi.ReminderFragment
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val fragments = mutableListOf(ReminderFragment(), ChatFragment(), ContactsFragment())
    private val titles = mutableListOf("Reminder", "Chat", "Kontak")
    private val icons = arrayOf(
        R.drawable.ic_reminder_white_24dp,
        R.drawable.ic_chat_white_24dp,
        R.drawable.ic_contact_white_24dp
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//            R.id.logout -> {
//                logout()
//                startActivity(Intent(this, SignInActivity::class.java))
//                finish()
//            }

            R.id.profile -> {
                when (user.category) {
                    User.USER_CATEGORY_SUPERVISI -> {
                        val i = Intent(this, SupervisiProfileActivity::class.java)
                        i.putExtra(Config.ARGS_USER, gson.toJson(user))
                        startActivity(i)
                    }
                    User.USER_CATEGORY_PMO -> {
                        val i = Intent(this, PMOProfileActivity::class.java)
                        i.putExtra(Config.ARGS_USER, gson.toJson(user))
                        startActivity(i)
                    }
                    User.USER_CATEGORY_PASIEN -> {
                        val i = Intent(this, PasienProfileActivity::class.java)
                        i.putExtra(Config.ARGS_USER, gson.toJson(user))
                        startActivity(i)
                    }
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
