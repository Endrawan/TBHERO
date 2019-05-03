package com.tbhero.application.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbhero.application.R.layout.item_linear_contact
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.item_linear_contact.view.*

class ContactsAdapter(private val users: MutableList<User>, private val action: (User) -> Unit) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private lateinit var ctx: Context

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(ctx: Context, user: User, action: (User) -> Unit) {
            itemView.name.text = user.name
            itemView.category.text = User.USER_CATEGORIES[user.category!!]
            itemView.setOnClickListener { action(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        ctx = parent.context

        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                item_linear_contact,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ContactViewHolder, p1: Int) {
        holder.bindItem(ctx, users[p1], action)
    }
}