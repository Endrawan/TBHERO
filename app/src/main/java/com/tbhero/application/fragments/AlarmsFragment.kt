package com.tbhero.application.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.tbhero.application.R
import com.tbhero.application.activities.MinumActivity
import com.tbhero.application.activities.PeriksaActivity
import com.tbhero.application.adapters.AlarmsAdapter
import com.tbhero.application.components.Fragment
import kotlinx.android.synthetic.main.fragment_alarms.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AlarmsFragment : Fragment() {

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
        drink.setOnClickListener {
            startActivity(Intent(activity, MinumActivity::class.java))
        }
        check.setOnClickListener {
            startActivity(Intent(activity, PeriksaActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = AlarmsAdapter(arrayOf("Paracetamol", "Adderall", "Heroin")) {
            Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }


}
