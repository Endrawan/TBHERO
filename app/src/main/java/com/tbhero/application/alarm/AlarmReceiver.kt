package com.tbhero.application.alarm

import android.app.*
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.gson.Gson
import com.tbhero.application.R
import com.tbhero.application.activities.MainActivity
import com.tbhero.application.models.Alarm
import com.tbhero.application.models.Config
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "com.tbhero.application.channelId"
    private lateinit var alarm: Alarm
    private val gson = Gson()

    override fun onReceive(context: Context?, intent: Intent?) {
        alarm = gson.fromJson(intent?.getStringExtra(Config.ARGS_ALARM), Alarm::class.java)
        val notificationIntent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(alarm.id.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = Notification.Builder(context)

        val notification = builder.setContentTitle("TBHERO")
            .setContentTitle("${alarm.getTimeVersion()} - ${Alarm.CATEGORY_SUBJECTS[alarm.category!!]}")
            .setContentText("${alarm.desc}")
            .setTicker("Notifikasi Baru!")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
        }

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "NotificationDemo",
                IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        if (isShowedToday(alarm)) notificationManager.notify(alarm.hashCode(), notification)
    }

    private fun isShowedToday(alarm: Alarm): Boolean {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> alarm.getRepeatDayStatus(Alarm.DAY_MONDAY)
            Calendar.TUESDAY -> alarm.getRepeatDayStatus(Alarm.DAY_TUESDAY)
            Calendar.WEDNESDAY -> alarm.getRepeatDayStatus(Alarm.DAY_WEDNESDAY)
            Calendar.THURSDAY -> alarm.getRepeatDayStatus(Alarm.DAY_THURSDAY)
            Calendar.FRIDAY -> alarm.getRepeatDayStatus(Alarm.DAY_FRIDAY)
            Calendar.SATURDAY -> alarm.getRepeatDayStatus(Alarm.DAY_SATURDAY)
            Calendar.SUNDAY -> alarm.getRepeatDayStatus(Alarm.DAY_SUNDAY)
            else -> false
        }
    }


}