package com.tbhero.application.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbhero.application.R.layout.item_linear_alarm
import com.tbhero.application.models.Alarm
import kotlinx.android.synthetic.main.item_linear_alarm.view.*

class AlarmsAdapter(private val alarms: MutableList<Alarm>, private val action: (Alarm) -> Unit) :
    RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder>() {

    private lateinit var ctx: Context

    class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindItem(alarm: Alarm, action: (Alarm) -> Unit) {
            itemView.name.text = Alarm.CATEGORY_SUBJECTS[alarm.category!!]
            itemView.time.text = alarm.getTimeVersion()
            itemView.setOnClickListener { action(alarm) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        ctx = parent.context

        return AlarmViewHolder(
            LayoutInflater.from(parent.context).inflate(
                item_linear_alarm,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = alarms.size

    override fun onBindViewHolder(holder: AlarmViewHolder, p1: Int) {
        holder.bindItem(alarms[p1], action)
    }
}