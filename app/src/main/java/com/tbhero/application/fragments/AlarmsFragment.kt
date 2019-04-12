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
import com.tbhero.application.activities.AlarmActivity
import com.tbhero.application.activities.BuyMedicineActivity
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
    private lateinit var mAct: AlarmActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAct = act as AlarmActivity
        initRecyclerView()
        drink.setOnClickListener {
            val i = Intent(activity, MinumActivity::class.java)
            val al = Alarm()
            al.name = mAct.patient.name
            i.putExtra(Config.ARGS_PATIENT, act.gson.toJson(mAct.patient))
            i.putExtra(Config.ARGS_ACTIVITY_STATUS, Config.VALUE_ACTIVITY_STATUS_CREATE)
            i.putExtra(Config.ARGS_ALARM, act.gson.toJson(al))
            startActivity(i)
        }
        check.setOnClickListener {
            val i = Intent(activity, PeriksaActivity::class.java)
            val al = Alarm()
            al.name = mAct.patient.name
            i.putExtra(Config.ARGS_PATIENT, act.gson.toJson(mAct.patient))
            i.putExtra(Config.ARGS_ACTIVITY_STATUS, Config.VALUE_ACTIVITY_STATUS_CREATE)
            i.putExtra(Config.ARGS_ALARM, act.gson.toJson(al))
            startActivity(i)
        }
    }

    private fun initRecyclerView() {
        lateinit var i: Intent
        val adapter = AlarmsAdapter(alarms) {
            when (it.category) {
                Alarm.CATEGORY_PERIKSA -> {
                    i = Intent(act, PeriksaActivity::class.java)
                }
                Alarm.CATEGORY_FASE_LANJUTAN, Alarm.CATEGORY_FASE_AWAL -> {
                    i = Intent(act, MinumActivity::class.java)
                }
                Alarm.CATEGORY_BELI_OBAT -> {
                    i = Intent(act, BuyMedicineActivity::class.java)
                    i.putExtra(Config.ARGS_MEDICINE_ALARM, act.gson.toJson(it))
                }
            }
            i.putExtra(Config.ARGS_PATIENT, act.gson.toJson(mAct.patient))
            i.putExtra(Config.ARGS_ACTIVITY_STATUS, Config.VALUE_ACTIVITY_STATUS_UPDATE)
            i.putExtra(Config.ARGS_ALARM, act.gson.toJson(it))
            startActivity(i)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        val mAct = act as AlarmActivity
        act.db.alarms.child(mAct.patient.id!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

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
