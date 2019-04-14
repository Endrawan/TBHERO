package com.tbhero.application.components

class Extension {
    companion object {
        fun isEmailValid(email: CharSequence?): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPhoneValid(phone: String?): Boolean {
            return android.util.Patterns.PHONE.matcher(phone).matches()
        }
    }
}