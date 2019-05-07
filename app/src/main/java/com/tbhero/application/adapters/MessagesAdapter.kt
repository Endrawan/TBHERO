package com.tbhero.application.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbhero.application.R.layout.item_linear_message_received
import com.tbhero.application.R.layout.item_linear_message_sent
import com.tbhero.application.models.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.item_linear_message_received.view.text as receivedText
import kotlinx.android.synthetic.main.item_linear_message_received.view.timestamp as receivedTimestamp
import kotlinx.android.synthetic.main.item_linear_message_sent.view.text as sentText
import kotlinx.android.synthetic.main.item_linear_message_sent.view.timestamp as sentTimestamp

class MessagesAdapter(
    private val messages: MutableList<Message>,
    val userId: String,
    private val action: (Message) -> Unit
) :
    RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    private val SENT_VIEW_TYPE: Int = 0
    private val RECEIVED_VIEW_TYPE: Int = 1
    private lateinit var ctx: Context

    abstract class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bindItem(ctx: Context, message: Message, action: (Message) -> Unit)
    }

    class MessageSentViewHolder(view: View) : MessageViewHolder(view) {
        override fun bindItem(ctx: Context, message: Message, action: (Message) -> Unit) {
            itemView.sentText.text = message.text

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = message.timestamp!!
            val format = "EEEE, dd MMMM yyyy HH:mm"
            val locale = Locale("in", "ID")
            val sdf = SimpleDateFormat(format, locale)
            itemView.sentTimestamp.text = sdf.format(calendar.time)
        }
    }

    class MessageReceivedViewHolder(view: View) : MessageViewHolder(view) {

        override fun bindItem(ctx: Context, message: Message, action: (Message) -> Unit) {
            itemView.receivedText.text = message.text

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = message.timestamp!!
            val format = "EEEE, dd MMMM yyyy HH:mm"
            val locale = Locale("in", "ID")
            val sdf = SimpleDateFormat(format, locale)
            itemView.receivedTimestamp.text = sdf.format(calendar.time)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == userId) SENT_VIEW_TYPE else RECEIVED_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        ctx = parent.context

        return when (viewType) {
            SENT_VIEW_TYPE -> MessageSentViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    item_linear_message_sent,
                    parent,
                    false
                )
            )
            else -> MessageReceivedViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    item_linear_message_received,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, p1: Int) {
        holder.bindItem(ctx, messages[p1], action)
    }
}