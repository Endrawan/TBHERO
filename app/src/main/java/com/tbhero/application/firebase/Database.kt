package com.tbhero.application.firebase

import com.google.firebase.database.FirebaseDatabase
import com.tbhero.application.models.User

class Database {
    private val database = FirebaseDatabase.getInstance()
    val users = database.reference.child("users")
    val pmos = users.orderByChild("category").equalTo(User.USER_CATEGORY_PMO.toDouble())
    val pasiens = users.orderByChild("category").equalTo(User.USER_CATEGORY_PASIEN.toDouble())
    val alarms = database.reference.child("alarms")
}