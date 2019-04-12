package com.tbhero.application

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class TBHeroApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}