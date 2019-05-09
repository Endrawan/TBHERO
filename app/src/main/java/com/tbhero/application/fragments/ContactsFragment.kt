package com.tbhero.application.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
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

    private val TAG = "ContactsFragment"
    val contacts = mutableListOf<User>()
    lateinit var adapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ContactsAdapter(contacts) {
            val i = Intent(activity, MessageActivity::class.java)
            i.putExtra(Config.ARGS_USER, act.gson.toJson(it))
            startActivity(i)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(act)

        when (act.user.category) {
            User.USER_CATEGORY_PASIEN -> initPatient()
            User.USER_CATEGORY_PMO -> initPMO()
            User.USER_CATEGORY_SUPERVISI -> initSupervisi()
        }
    }

    private fun initPatient() {
        act.db.users.child(act.user.pmoId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Get pmo error - ${error.message}")
            }

            override fun onDataChange(data: DataSnapshot) {
                Log.d(TAG, "Get pmo value : $data")
                val pmo = data.getValue(User::class.java)!!
                var found = false
                for (i in contacts.indices) {
                    if (pmo.id == contacts[i].id) {
                        found = true
                        contacts[i] = pmo
                        adapter.notifyItemChanged(i)
                        break
                    }
                }
                if (!found) {
                    contacts.add(pmo)
                    adapter.notifyItemInserted(contacts.size - 1)
                }
            }

        })

        act.db.users.orderByChild("category").equalTo(User.USER_CATEGORY_SUPERVISI.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Get supervisi error - ${error.message}")
                }

                override fun onDataChange(data: DataSnapshot) {
                    Log.d(TAG, "Get supervisi success = $data")
                    data.children.forEach {
                        val supervisi = it.getValue(User::class.java)!!
                        var found = false
                        for (i in contacts.indices) {
                            if (supervisi.id == contacts[i].id) {
                                found = true
                                contacts[i] = supervisi
                                adapter.notifyItemChanged(i)
                                break
                            }
                        }
                        if (!found) {
                            contacts.add(supervisi)
                            adapter.notifyItemInserted(contacts.size - 1)
                        }
                    }
                }

            })
    }

    private fun initPMO() {
        act.db.users.child(act.user.pasienId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Get pasien error - ${error.message}")
            }

            override fun onDataChange(data: DataSnapshot) {
                Log.d(TAG, "Get pasien value : $data")
                val pasien = data.getValue(User::class.java)!!
                var found = false
                for (i in contacts.indices) {
                    if (pasien.id == contacts[i].id) {
                        found = true
                        contacts[i] = pasien
                        adapter.notifyItemChanged(i)
                        break
                    }
                }
                if (!found) {
                    contacts.add(pasien)
                    adapter.notifyItemInserted(contacts.size - 1)
                }
            }

        })
    }

    private fun initSupervisi() {
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
