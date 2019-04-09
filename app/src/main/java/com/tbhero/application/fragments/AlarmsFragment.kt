package com.tbhero.application.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.tbhero.application.R
import com.tbhero.application.activities.AlarmActivity
import com.tbhero.application.activities.MinumActivity
import com.tbhero.application.activities.PeriksaActivity
import com.tbhero.application.adapters.AlarmsAdapter
import com.tbhero.application.components.Fragment
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import kotlinx.android.synthetic.main.fragment_alarms.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AlarmsFragment : Fragment() {

    val alarms = mutableListOf<Alarm>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        val mAct = act as AlarmActivity
        drink.setOnClickListener {
            val i = Intent(activity, MinumActivity::class.java)
            i.putExtra(Config.ARGS_PATIENT, act.gson.toJson(mAct.patient))
            startActivity(i)
        }
        check.setOnClickListener {
            val i = Intent(activity, PeriksaActivity::class.java)
            i.putExtra(Config.ARGS_PATIENT, act.gson.toJson(mAct.patient))
            startActivity(i)
        }
    }

    private fun initRecyclerView() {
        val adapter = AlarmsAdapter(alarms) {
            Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        val mAct = act as AlarmActivity
        act.db.alarms.child(mAct.patient.id!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                alarms.clear()
                for (snapshot in dataSnapshot.children)
                    alarms.add(snapshot.getValue(Alarm::class.java)!!)
                adapter.notifyDataSetChanged()
            }

        })
    }

}