package com.tbhero.application.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbhero.application.R.layout.item_linear_alarm
import kotlinx.android.synthetic.main.item_linear_user.view.*

class AlarmsAdapter(private val data: Array<String>, private val action: (String) -> Unit) :
    RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder>() {

    private lateinit var ctx: Context

    class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindItem(ctx: Context, data: String, action: (String) -> Unit) {
            itemView.name.text = data
            itemView.setOnClickListener { action(data) }
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

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: AlarmViewHolder, p1: Int) {
        holder.bindItem(ctx, data[p1], action)
    }
}