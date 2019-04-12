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
import com.tbhero.application.activities.BuyMedicineActivity
import com.tbhero.application.activities.MinumActivity
import com.tbhero.application.activities.PeriksaActivity
import com.tbhero.application.adapters.AlarmsAdapter
import com.tbhero.application.adapters.UsersAdapter
import com.tbhero.application.components.Fragment
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.fragment_reminder.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ReminderFragment : Fragment() {

    lateinit var patient: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        when (act.user.category) {
            User.USER_CATEGORY_SUPERVISI -> initSupervisi()
            User.USER_CATEGORY_PMO -> initPMO()
            User.USER_CATEGORY_PASIEN -> initPatient()
        }

    }

    private fun initSupervisi() {
        val pasienList = mutableListOf<User>()
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
                toast("Gagal mengambil data")
            }
        })
    }

    private fun initPatient() {
        patientMsg.visibility = View.VISIBLE

        val alarms = mutableListOf<Alarm>()
        val adapter = AlarmsAdapter(alarms) {
            setItemAction(it, act.user)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        act.db.alarms.child(act.user.id!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                alarms.clear()
                for (snapshot in data.children) {
                    val alarm = snapshot.getValue(Alarm::class.java)!!
                    alarms.add(alarm)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                toast("Gagal mengambil data")
            }
        })
    }

    private fun initPMO() {
        lookProfile.setOnClickListener {

        }
        act.db.users.child(act.user.pasienId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast("Gagal mengambil data")
            }

            override fun onDataChange(data: DataSnapshot) {
                patient = data.getValue(User::class.java)!!

                patientName.text = patient.name
                pmoMsg.visibility = View.VISIBLE
                initPMORecycelerView()
            }

        })

    }

    private fun initPMORecycelerView() {
        val alarms = mutableListOf<Alarm>()
        val adapter = AlarmsAdapter(alarms) {
            setItemAction(it, patient)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        act.db.alarms.child(patient.id!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                alarms.clear()
                for (snapshot in data.children) {
                    val alarm = snapshot.getValue(Alarm::class.java)!!
                    alarms.add(alarm)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                toast("Gagal mengambil data")
            }
        })
    }

    private fun setItemAction(it: Alarm, patient: User) {
        lateinit var i: Intent
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
        i.putExtra(Config.ARGS_PATIENT, act.gson.toJson(patient))
        i.putExtra(Config.ARGS_ACTIVITY_STATUS, Config.VALUE_ACTIVITY_STATUS_READ_ONLY)
        i.putExtra(Config.ARGS_ALARM, act.gson.toJson(it))
        startActivity(i)
    }

}
