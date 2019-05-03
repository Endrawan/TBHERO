package com.tbhero.application.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.tbhero.application.R
import com.tbhero.application.activities.MessageActivity
import com.tbhero.application.adapters.ContactsAdapter
import com.tbhero.application.components.Fragment
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.fragment_contacts.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ContactsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (act.user.category) {
            User.USER_CATEGORY_PASIEN -> initPatient()
            User.USER_CATEGORY_PMO -> initPMO()
            User.USER_CATEGORY_SUPERVISI -> initSupervisi()
        }
    }

    private fun initPatient() {

    }

    private fun initPMO() {

    }

    private fun initSupervisi() {
        val contacts = mutableListOf<User>()
        val adapter = ContactsAdapter(contacts) {
            val i = Intent(activity, MessageActivity::class.java)
            i.putExtra(Config.ARGS_USER, act.gson.toJson(it))
            startActivity(i)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(act)

        val ref = act.db.pasiens
        ref.keepSynced(true)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                contacts.clear()
                for (snapshot in data.children) {
                    val pasien = snapshot.getValue(User::class.java)!!
                    contacts.add(pasien)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                toast("Gagal mengambil data")
            }
        })
    }


}
