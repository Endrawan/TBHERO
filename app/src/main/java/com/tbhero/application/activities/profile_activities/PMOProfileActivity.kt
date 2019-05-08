package com.tbhero.application.activities.profile_activities

import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tbhero.application.R.layout.activity_pmo_profile
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_pmo_profile.*

class PMOProfileActivity : AppCompatActivity() {

    lateinit var PMO: User
    val TAG = "PMOProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_pmo_profile)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        PMO = gson.fromJson(intent.getStringExtra(Config.ARGS_USER), User::class.java)
        name.text = PMO.name
        phone.text = PMO.phone
        email.text = PMO.email
//        address.text = PMO.add


        // Load the patient
        db.users.child(PMO.pasienId!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "error loading patient - ${error.message}")
            }

            override fun onDataChange(data: DataSnapshot) {
                Log.d(TAG, "success loading patient : $data")
                val patient = data.getValue(User::class.java)
                pasien.text = patient?.name
            }

        })
    }
}
