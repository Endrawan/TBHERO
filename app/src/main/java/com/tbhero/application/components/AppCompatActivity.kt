package com.tbhero.application.components

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tbhero.application.alarm.AlarmReceiver
import com.tbhero.application.firebase.Database
import com.tbhero.application.models.Config
import com.tbhero.application.models.User


open class AppCompatActivity : AppCompatActivity() {
    private val TAG = "AppCompatActivity"
    protected val auth = FirebaseAuth.getInstance()
    protected var firebaseUser = auth.currentUser
    var user: User = User()
    val db = Database()
    val gson = Gson()
    protected lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SP = getSharedPreferences(Config.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
        user = getUserFromSP()
    }

    protected fun updateUser(onUserFound: () -> Unit, onUserNotFound: () -> Unit) {
        firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            db.users.child(firebaseUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        user = dataSnapshot.getValue(User::class.java)!!
                        writeUserToSP(user)
                        onUserFound()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(TAG, "loadPost:onCancelled", error.toException())
                    }
                })
        } else {
            onUserNotFound()
        }
    }

    fun writeToSP(key: String, value: String) {
        val editor = SP.edit()
        editor.putString(key, value)
        editor.apply()
    }

    protected fun writeUserToSP(user: User) {
        val editor = SP.edit()
        editor.putString(Config.SP_USER, gson.toJson(user))
        editor.apply()
    }

    protected fun getUserFromSP(): User {
        val json = SP.getString(Config.SP_USER, null)
        return gson.fromJson(json, User::class.java) ?: User()
    }

    private fun removeFromSP(tag: String) {
        val editor = SP.edit()
        editor.remove(tag)
        editor.apply()
    }

    protected fun logout() {
        removeActiveAlarm()
        removeFromSP(Config.SP_USER)
        auth.signOut()
    }

    fun removeActiveAlarm() {
        val json = SP.getString(Config.SP_ALARM_CODES, null)
        val mutableListType = object : TypeToken<ArrayList<Int>>() {}.type
        val alarmCodes: MutableList<Int>? = gson.fromJson(json, mutableListType)
        val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(this, AlarmReceiver::class.java)
        if (alarmCodes != null)
            for (code in alarmCodes) {
                val pendingIntent = PendingIntent.getBroadcast(
                    this, code, myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmMgr.cancel(pendingIntent)
            }
        removeFromSP(Config.SP_ALARM_CODES)
    }

    protected fun toast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}