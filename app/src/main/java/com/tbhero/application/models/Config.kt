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
        val ARGS_MEDICINE_ALARM = "medicine_alarm"
        val ARGS_ACTIVITY_STATUS = "status"
        val ARGS_ID = "id"
        val VALUE_ACTIVITY_STATUS_READ_ONLY = 0
        val VALUE_ACTIVITY_STATUS_UPDATE = 2
        val VALUE_ACTIVITY_STATUS_CREATE = 1

        val CHANNEL_ID = "666"
    }
}