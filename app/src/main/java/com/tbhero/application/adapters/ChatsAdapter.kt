package com.tbhero.application.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbhero.application.R.layout.item_linear_chat
import com.tbhero.application.models.Chat
import kotlinx.android.synthetic.main.item_linear_chat.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatsAdapter(private val chats: MutableList<Chat>, private val action: (Chat) -> Unit) :
    RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {

    private lateinit var ctx: Context

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(ctx: Context, chat: Chat, action: (Chat) -> Unit) {
            itemView.name.text = chat.recipientName
            itemView.recentText.text = chat.recentMessage
            itemView.notification.text = chat.notifCount.toString()


            val calendar = Calendar.getInstance()
            calendar.timeInMillis = chat.timestamp!!
            val format = "HH:mm"
            val locale = Locale("in", "ID")
            val sdf = SimpleDateFormat(format, locale)
            itemView.time.text = sdf.format(calendar.time)

            itemView.setOnClickListener { action(chat) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        ctx = parent.context

        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                item_linear_chat,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatViewHolder, p1: Int) {
        holder.bindItem(ctx, chats[p1], action)
    }
}