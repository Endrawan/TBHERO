package com.tbhero.application.fragments.supervisi


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
import com.tbhero.application.activities.AlarmActivity
import com.tbhero.application.adapters.UsersAdapter
import com.tbhero.application.components.Fragment
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.fragment_reminder.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ReminderFragment : Fragment() {

    private val pasienList = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val adapter = UsersAdapter(pasienList) {
            val i = Intent(activity, AlarmActivity::class.java)
            i.putExtra(Config.ARGS_PATIENT, act.gson.toJson(it))
            startActivity(i)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        act.db.pasiens.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                pasienList.clear()
                for (snapshot in data.children) {
                    val pasien = snapshot.getValue(User::class.java)!!
                    pasienList.add(pasien)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
//                pmo.hideProgress()
                toast("Gagal mengambil data")
            }
        })
    }


}
