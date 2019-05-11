package com.tbhero.application.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbhero.application.R.drawable.ic_arrow_down_grey_24dp
import com.tbhero.application.R.drawable.ic_arrow_up_grey_24dp
import com.tbhero.application.R.layout.item_linear_user
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.item_linear_user.view.*

class UsersAdapter(private val users: MutableList<User>, private val action: (User) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private lateinit var ctx: Context

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var isExpanded: Boolean = false

        fun bindItem(ctx: Context, user: User, action: (User) -> Unit) {
            itemView.name.text = user.name
            itemView.born.text = user.getBorn()
            if (user.address == null)
                itemView.address.text = "Alamat tidak ada"
            else
                itemView.address.text = user.address
            itemView.phone.text = user.phone
            itemView.setOnClickListener { action(user) }
            itemView.expand.setOnClickListener {
                if (isExpanded) {
                    itemView.expanded_menu.visibility = View.GONE
                    itemView.expand.setImageDrawable(ContextCompat.getDrawable(ctx, ic_arrow_down_grey_24dp))
                } else {
                    itemView.expanded_menu.visibility = View.VISIBLE
                    itemView.expand.setImageDrawable(ContextCompat.getDrawable(ctx, ic_arrow_up_grey_24dp))
                }
                isExpanded = !isExpanded
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        ctx = parent.context

        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                item_linear_user,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, p1: Int) {
        holder.bindItem(ctx, users[p1], action)
    }
}