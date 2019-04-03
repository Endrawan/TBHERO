package com.tbhero.application.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbhero.application.R
import com.tbhero.application.activities.AlarmActivity
import com.tbhero.application.adapters.UsersAdapter
import com.tbhero.application.components.Fragment
import kotlinx.android.synthetic.main.fragment_reminder.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ReminderFragment : Fragment() {

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
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = UsersAdapter(arrayOf("Tekashi", "Anuel Aa", "Los Intocables")) {
            startActivity(Intent(activity, AlarmActivity::class.java))
        }
    }


}
