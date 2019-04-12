package com.tbhero.application.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.tbhero.application.R.layout.view_button
import com.tbhero.application.R.styleable.*
import kotlinx.android.synthetic.main.view_button.view.*

class ButtonView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            ButtonView, 0, 0
        )
        val name = a.getString(ButtonView_buttonName)
        val padding = a.getDimensionPixelSize(ButtonView_android_padding, 0)
        a.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(view_button, this, true)

        button.text = name
//        if(padding != 0) button.setPadding(padding, padding, padding, padding)
    }

    override fun setOnClickListener(listener: OnClickListener) {
        button.setOnClickListener { listener.onClick(button) }
    }

    fun showProgress() {
        progressBar.visibility = android.view.View.VISIBLE
        cardView.visibility = android.view.View.INVISIBLE
    }

    fun hideProgress() {
        progressBar.visibility = android.view.View.GONE
        cardView.visibility = android.view.View.VISIBLE
    }

    fun getButton(): Button {
        return button
    }
}