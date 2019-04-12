package com.tbhero.application.models

import java.text.SimpleDateFormat
import java.util.*

data class Alarm(
    var id: String? = null,
    var category: Int? = null,
    var dosage: String? = null,
    var desc: String? = null,
    var hour: Int = 5,
    var minute: Int = 0,
    var repeat: MutableList<Boolean> = mutableListOf(false, false, false, false, false, false, false),
    var name: String? = null,
    var phaseCategory: Int? = null,
    var date: Long? = null
) {

    fun getTimeVersion(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val locale = Locale("in", "ID")
        val sdf = SimpleDateFormat("HH:mm", locale)
        return sdf.format(calendar.time)
    }

    fun getRepeatDayStatus(day: Int): Boolean {
        return repeat[day]
    }

    companion object {
        val CATEGORY_FASE_FASE = arrayOf("Fase Awal", "Fase Lanjutan")
        val CATEGORY_SUBJECTS = arrayOf("Minum Obat", "Minum Obat Lanjutan", "Beli Obat", "Periksa")
        const val CATEGORY_FASE_AWAL = 0
        const val CATEGORY_FASE_LANJUTAN = 1
        const val CATEGORY_BELI_OBAT = 2
        const val CATEGORY_PERIKSA = 3
        const val DAY_MONDAY = 0
        const val DAY_TUESDAY = 1
        const val DAY_WEDNESDAY = 2
        const val DAY_THURSDAY = 3
        const val DAY_FRIDAY = 4
        const val DAY_SATURDAY = 5
        const val DAY_SUNDAY = 6
    }
}