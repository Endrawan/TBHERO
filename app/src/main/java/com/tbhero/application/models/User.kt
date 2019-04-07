package com.tbhero.application.models

data class User(
    var id: String? = null,
    var category: Int? = null,
    var name: String? = null,
    var dateBorn: Long? = null,
    var weight: Float? = null,
    var pmoId: String? = null,
    var pasienId: String? = null,
    var puskesmas: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var password: String? = null
) {
    companion object {
        val USER_CATEGORIES = arrayOf("Pasien", "PMO", "Supervisi")
        val USER_CATEGORIES_CODE = arrayOf(0, 1, 2)
        const val USER_CATEGORY_PASIEN = 0
        const val USER_CATEGORY_PMO = 1
        const val USER_CATEGORY_SUPERVISI = 2
    }
}