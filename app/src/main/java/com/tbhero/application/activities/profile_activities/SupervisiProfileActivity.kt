package com.tbhero.application.activities.profile_activities

import android.os.Bundle
import com.tbhero.application.R.layout.activity_supervisi_profile
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_supervisi_profile.*

class SupervisiProfileActivity : ProfileActivity() {

    lateinit var supervisi: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_supervisi_profile)

        supervisi = gson.fromJson(intent.getStringExtra(Config.ARGS_USER), User::class.java)

        name.text = supervisi.name
        phone.text = supervisi.phone
        email.text = supervisi.email

        initToolbar(toolbar)
        setChatAction(supervisi)
    }

    override fun onStart() {
        super.onStart()
        refreshMenuItem(supervisi.id!!)
    }
}
