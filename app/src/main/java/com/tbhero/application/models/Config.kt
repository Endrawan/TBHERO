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
        val SP_ALARM_CODES = "alarm_codes"

        val ARGS_PATIENT = "patient"
        val ARGS_ALARM = "alarm"
        val ARGS_MEDICINE_ALARM = "medicine_alarm"
        val ARGS_ACTIVITY_STATUS = "status"
        val ARGS_ID = "id"
        val ARGS_USER = "user"
        val VALUE_ACTIVITY_STATUS_READ_ONLY = 0
        val VALUE_ACTIVITY_STATUS_UPDATE = 2
        val VALUE_ACTIVITY_STATUS_CREATE = 1

        val CHANNEL_ID = "666"

        val MIN_LENGTH_EMAIL = 6
        val MIN_LENGTH_PASSWORD = 6
        val MIN_LENGTH_NAME = 6
        val MIN_LENGTH_PHONE = 10
        val MIN_LENGTH_ADDRESS = 6
    }
}