package com.tbhero.application.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.tbhero.application.R.layout.activity_message
import com.tbhero.application.adapters.MessagesAdapter
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.models.Config
import com.tbhero.application.models.Message
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    lateinit var recipient: User
    lateinit var adapter: MessagesAdapter
    val messages = mutableListOf<Message>(Message("1251251", "wewaf123", "Hay", System.currentTimeMillis()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_message)

        recipient = gson.fromJson(intent.getStringExtra(Config.ARGS_USER), User::class.java)
        initView()
        send.setOnClickListener { send() }
    }

    private fun initView() {
        name.text = recipient.name

        adapter = MessagesAdapter(messages, user.id!!) {

        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun send() {
        val text = input.text.toString().trim()
        messages.add(Message("1251251", user.id!!, text, System.currentTimeMillis()))
        messages.add(Message("1251251", "Pray for tekashi", "This is your answer", System.currentTimeMillis()))

        input.text.clear()
        adapter.notifyDataSetChanged()
    }
}
