package com.tbhero.application.activities.profile_activities

import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.tbhero.application.R
import com.tbhero.application.activities.MessageActivity
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.models.Config
import com.tbhero.application.models.User


open class ProfileActivity : AppCompatActivity() {

    protected var chatItem: MenuItem? = null
    protected var logoutItem: MenuItem? = null
    protected var chatVisibility = true
    var profileUser: User? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        chatItem = menu?.findItem(R.id.chat)
        logoutItem = menu?.findItem(R.id.logout)

        if (!chatVisibility) chatItem?.isVisible = false
        else logoutItem?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.chat -> {
                if (profileUser != null) {
                    val i = Intent(this, MessageActivity::class.java)
                    i.putExtra(Config.ARGS_USER, gson.toJson(profileUser))
                    startActivity(i)
                    true
                } else false
            }
            R.id.logout -> {
                logout()
                val i = baseContext.packageManager
                    .getLaunchIntentForPackage(baseContext.packageName)
                i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun hideChatItem() {
        chatVisibility = false
        invalidateOptionsMenu()
    }

    protected fun refreshMenuItem(userId: String) {
        if (user.id == userId)
            hideChatItem()
    }

    protected fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    protected fun setChatAction(user: User) {
        profileUser = user
    }
}