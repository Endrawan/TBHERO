package com.tbhero.application.components

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class Extension {
    companion object {
        fun isEmailValid(email: CharSequence?): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPhoneValid(phone: String?): Boolean {
            return android.util.Patterns.PHONE.matcher(phone).matches()
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun getMd5(input: String): String {
            try {

                // Static getInstance method is called with hashing MD5
                val md = MessageDigest.getInstance("MD5")

                // digest() method is called to calculate message digest
                //  of an input digest() return array of byte
                val messageDigest = md.digest(input.toByteArray())

                // Convert byte array into signum representation
                val no = BigInteger(1, messageDigest)

                // Convert message digest into hex value
                var hashtext = no.toString(16)
                while (hashtext.length < 32) {
                    hashtext = "0$hashtext"
                }
                return hashtext
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            }
            // For specifying wrong message digest algorithms
        }
    }

}