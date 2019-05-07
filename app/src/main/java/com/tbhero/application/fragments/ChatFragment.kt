package com.tbhero.application.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.tbhero.application.R
import com.tbhero.application.activities.MessageActivity
import com.tbhero.application.adapters.ChatsAdapter
import com.tbhero.application.components.Fragment
import com.tbhero.application.models.Chat
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ChatFragment : Fragment() {

    private val TAG = "ChatFragment"

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
            act.db.users.child(it.recipientId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(data: DataSnapshot) {
                    Log.d(TAG, "Recipient value : $data")
                    val recipient = data.getValue(User::class.java)
                    val i = Intent(activity, MessageActivity::class.java)
                    i.putExtra(Config.ARGS_USER, act.gson.toJson(recipient))
                    startActivity(i)
                }

            })
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(act)
        loadChats()
    }

    private fun loadChats() {
        act.db.getChatsRef(act.user.id!!).orderByChild("reverseTimestamp")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                override fun onChildChanged(data: DataSnapshot, prevKey: String?) {
                    Log.d(TAG, "$prevKey")
                    var position = 0
                    if (prevKey == null) chats[0] = data.getValue(Chat::class.java)!!
                    else
                        for (i in chats.indices)
                            if (chats[i].id.equals(prevKey)) {
                                chats[i + 1] = data.getValue(Chat::class.java)!!
                                position = i + 1
                                break
                            }
                    adapter.notifyItemChanged(position)
                }

                override fun onChildAdded(data: DataSnapshot, p1: String?) {
                    Log.d(TAG, "Chat value = $data")
                    val chat = data.getValue(Chat::class.java)!!
                    chats.add(chat)
                    adapter.notifyItemInserted(chats.size)
                }

                override fun onChildRemoved(p0: DataSnapshot) {}
            })
    }


}
