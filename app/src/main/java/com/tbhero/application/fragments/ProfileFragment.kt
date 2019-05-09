package com.tbhero.application.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.tbhero.application.R.layout.fragment_profile
import com.tbhero.application.activities.profile_activities.PMOProfileActivity
import com.tbhero.application.activities.profile_activities.PasienProfileActivity
import com.tbhero.application.components.Fragment
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    private val TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = act as PasienProfileActivity
        val pasien = activity.pasien

        born.text = pasien.dateBorn.toString()
        weight.text = pasien.weight.toString()
        phone.text = pasien.phone
//        address.text = user.address
//        phaseLabel = user


        act.db.users.child(pasien.pmoId!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to load PMO - ${error.message}")
            }

            override fun onDataChange(data: DataSnapshot) {
                Log.d(TAG, "Success to load PMO = $data")
                val PMO = data.getValue(User::class.java)
                pmo.text = PMO?.name
                pmo.setOnClickListener {
                    val i = Intent(act, PMOProfileActivity::class.java)
                    i.putExtra(Config.ARGS_USER, act.gson.toJson(PMO))
                    startActivity(i)
                }
            }

        })

    }


}
