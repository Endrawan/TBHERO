package com.tbhero.application.models

class Config {
    companion object {
        val PUSKESMAS_LIST = arrayOf(
            "Puskesmas Polowijen",
            "Puskesmas Brawijaya",
            "Puskesmas Padjajaran",
            "Puskesmas Singaparna"
        )
        val PUSKESMAS_CODES = arrayOf(0, 1, 2, 3)

        val PREFERENCE_FILE_KEY = "TBHERO"
        val SP_USER = "user"

        val ARGS_PATIENT = "patient"
        val ARGS_ALARM = "alarm"
    }
}