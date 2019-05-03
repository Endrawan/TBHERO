package com.tbhero.application.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tbhero.application.R
import com.tbhero.application.activities.MessageActivity
import com.tbhero.application.adapters.ChatsAdapter
import com.tbhero.application.components.Fragment
import com.tbhero.application.models.Chat
import com.tbhero.application.models.Config
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ChatFragment : Fragment() {

    lateinit var adapter: ChatsAdapter
    val chats = mutableListOf<Chat>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatsAdapter(chats) {
            val i = Intent(activity, MessageActivity::class.java)
            i.putExtra(Config.ARGS_USER, act.gson.toJson(it))
            startActivity(i)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(act)

        chats.add(Chat("2412215", "421531", "Tekashi69", "What up yoooo", 69, System.currentTimeMillis()))
        chats.add(
            Chat(
                "2412215",
                "421531",
                "The 1975",
                "Hey i'm coming to indonesia this year, will you watch us?",
                4,
                System.currentTimeMillis()
            )
        )
    }


}
