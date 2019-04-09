package com.tbhero.application.models

data class Alarm(
    var id: String? = null,
    var category: Int? = null,
    var dosage: String? = null,
    var desc: String? = null,
    var time: String? = null,
    var repeat: MutableList<Boolean> = mutableListOf(false, false, false, false, false, false, false),
    var name: String? = null,
    var phaseCategory: Int? = null,
    var date: Long? = null
) {

    companion object {
        val CATEGORY_FASE_FASE = arrayOf("Fase Awal", "Fase Lanjutan")
        val CATEGORY_SUBJECTS = arrayOf("Minum Obat", "Minum Obat Lanjutan", "Beli Obat", "Periksa")
        const val CATEGORY_FASE_AWAL = 0
        const val CATEGORY_FASE_LANJUTAN = 1
        const val CATEGORY_BELI_OBAT = 2
        const val CATEGORY_PERIKSA = 3
    }
}