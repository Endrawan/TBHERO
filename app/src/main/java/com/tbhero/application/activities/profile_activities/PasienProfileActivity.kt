package com.tbhero.application.activities.profile_activities

import android.os.Bundle
import com.tbhero.application.R.drawable.ic_reminder_white_24dp
import com.tbhero.application.R.drawable.ic_user_white_24dp
import com.tbhero.application.R.layout.activity_pasien_profile
import com.tbhero.application.adapters.MainPagerAdapter
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.fragments.ProfileFragment
import com.tbhero.application.fragments.StatisticsFragment
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_pasien_profile.*

class PasienProfileActivity : AppCompatActivity() {

    private val fragments = mutableListOf(ProfileFragment(), StatisticsFragment())
    private val titles = mutableListOf("Profil", "Statistik")
    private val icons = arrayOf(
        ic_user_white_24dp,
        ic_reminder_white_24dp
    )

    lateinit var pasien: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_pasien_profile)

        pasien = gson.fromJson(intent.getStringExtra(Config.ARGS_USER), User::class.java)

        initToolbar()
        initTabLayout()
        initView()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    private fun initTabLayout() {
        val adapter = MainPagerAdapter(supportFragmentManager, fragments, titles)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun initView() {
        name.text = pasien.name
    }
}
