package com.tbhero.application.components

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast

open class Fragment : Fragment() {
    lateinit var act: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as AppCompatActivity
    }

    fun toast(text: String) {
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show()
    }
}