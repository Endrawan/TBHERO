package com.tbhero.application.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.tbhero.application.R.layout.activity_message
import com.tbhero.application.activities.profile_activities.PMOProfileActivity
import com.tbhero.application.activities.profile_activities.PasienProfileActivity
import com.tbhero.application.activities.profile_activities.SupervisiProfileActivity
import com.tbhero.application.adapters.MessagesAdapter
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.components.Extension
import com.tbhero.application.models.Chat
import com.tbhero.application.models.Config
import com.tbhero.application.models.Message
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    private val TAG = "MessageActivity"
    var myChat: Chat? = null
    var recipientChat: Chat? = null
    lateinit var recipient: User
    lateinit var adapter: MessagesAdapter
    val messages = mutableListOf<Message>()

    var getMyChatStatus = false
    var getRecipientChatStatus = false
    var sendMyChat = false
    var sendRecipientChat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_message)

        recipient = gson.fromJson(intent.getStringExtra(Config.ARGS_USER), User::class.java)
        initView()
        send.setOnClickListener { send() }
    }

    private fun initView() {
        getCurrentChat()
        name.text = recipient.name
        name.setOnClickListener {
            when (recipient.category) {
                User.USER_CATEGORY_SUPERVISI -> {
                    val i = Intent(this, SupervisiProfileActivity::class.java)
                    i.putExtra(Config.ARGS_USER, gson.toJson(recipient))
                    startActivity(i)
                }
                User.USER_CATEGORY_PMO -> {
                    val i = Intent(this, PMOProfileActivity::class.java)
                    i.putExtra(Config.ARGS_USER, gson.toJson(recipient))
                    startActivity(i)
                }
                User.USER_CATEGORY_PASIEN -> {
                    val i = Intent(this, PasienProfileActivity::class.java)
                    i.putExtra(Config.ARGS_USER, gson.toJson(recipient))
                    startActivity(i)
                }
            }
        }

        adapter = MessagesAdapter(messages, user.id!!) {

        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun getCurrentChat() {
        Log.d(TAG, "Recipient id value : ${recipient.id}")
        db.getChatsRef(user.id!!).orderByChild("recipientId").equalTo(recipient.id)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

                override fun onChildAdded(data: DataSnapshot, p1: String?) {
                    Log.d(TAG, "My Data value = $data")
                    myChat = data.getValue(Chat::class.java)!!
                    getMyChatStatus = true
                    getConversation()
                }

                override fun onChildRemoved(p0: DataSnapshot) {}

            })

//        db.getChatsRef(user.id!!).orderByChild("recipientId").equalTo(recipient.id)
//            .addValueEventListener(object: ValueEventListener{
//                override fun onCancelled(data: DatabaseError) {
//
//                }
//
//                override fun onDataChange(data: DataSnapshot) {
//                    Log.d(TAG, "My Data value = $data")
//                    data.children.forEach {
//                        myChat = it.getValue(Chat::class.java)!!
//                    }
//                    getMyChatStatus = true
//                    getConversation()
//                    if(myChat != null) getConversation()
//                }
//
//            })

        db.getChatsRef(recipient.id!!).orderByChild("recipientId").equalTo(user.id)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

                override fun onChildAdded(data: DataSnapshot, p1: String?) {
                    Log.d(TAG, "Recipient data value = $data")
                    recipientChat = data.getValue(Chat::class.java)!!
                    getRecipientChatStatus = true
                    getConversation()
                }

                override fun onChildRemoved(p0: DataSnapshot) {}

            })

//        db.getChatsRef(recipient.id!!).orderByChild("recipientId").equalTo(user.id)
//            .addValueEventListener(object:ValueEventListener{
//                override fun onCancelled(p0: DatabaseError) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//                override fun onDataChange(data: DataSnapshot) {
//                    Log.d(TAG, "Recipient data value = $data")
//                    data.children.forEach {
//                        recipientChat = it.getValue(Chat::class.java)!!
//                    }
//                    toast(recipientChat?.id)
//                    getRecipientChatStatus = true
//                    if(recipientChat != null)getConversation()
//                }
//
//            })
    }

    private fun getConversation() {
        if (!getMyChatStatus || !getRecipientChatStatus) return
        toast("Load Complete!")
        db.getMessageRef(myChat!!.id!!).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(data: DataSnapshot, p1: String?) {
                Log.d(TAG, "Message: $data")
                messages.add(data.getValue(Message::class.java)!!)
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.smoothScrollToPosition(messages.size)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun send() {
        val text = input.text.toString().trim()
        if (text.isEmpty()) return
        val timestamp = System.currentTimeMillis()
        val message = Message(null, user.id, user.name, text, timestamp)
        if (myChat == null && recipientChat == null) {
            Log.d(TAG, "My and recipient chat are null")
            val hashId = Extension.getMd5(timestamp.toString())
            myChat = Chat(
                hashId,
                recipient.id!!,
                recipient.name!!,
                user.id!!,
                user.name!!,
                text,
                1,
                timestamp,
                -1 * timestamp
            )
            recipientChat =
                Chat(hashId, user.id!!, user.name!!, user.id!!, user.name!!, text, 1, timestamp, -1 * timestamp)
        } else {
            Log.d(TAG, "My and recipient chat not null")
            myChat!!.newMessage(message)
            recipientChat!!.newMessage(message)
        }
        sendChatToDB(message)
        input.text.clear()
    }

    private fun sendChatToDB(message: Message) {
        val myChatRef = db.getChatsRef(user.id!!).child(myChat!!.id!!)
        val recipientChatRef = db.getChatsRef(recipient.id!!).child(recipientChat!!.id!!)

        myChatRef.setValue(myChat).addOnSuccessListener {
            sendMyChat = true
            sendMsgToDB(myChat!!.id!!, message)
        }
        recipientChatRef.setValue(recipientChat).addOnSuccessListener {
            sendRecipientChat = true
            sendMsgToDB(recipientChat!!.id!!, message)

        }
    }

    private fun sendMsgToDB(chatId: String, message: Message) {
        if (!sendMyChat || !sendRecipientChat) return
        val ref = db.messages.child(chatId).push()
        message.id = ref.key
        ref.setValue(message).addOnSuccessListener {
            toast("Berhasil menambah message")
            sendMyChat = false
            sendRecipientChat = false
        }.addOnFailureListener {
            toast(it.message)
            sendMyChat = false
            sendRecipientChat = false
        }
    }
}
