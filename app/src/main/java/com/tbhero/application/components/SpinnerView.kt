package com.tbhero.application.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import com.tbhero.application.R.layout.view_spinner
import com.tbhero.application.R.styleable.SpinnerView
import com.tbhero.application.R.styleable.SpinnerView_android_entries
import kotlinx.android.synthetic.main.view_spinner.view.*

class SpinnerView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            SpinnerView, 0, 0
        )
        val entries = a.getTextArray(SpinnerView_android_entries)
        a.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(view_spinner, this, true)

        if (entries != null) {
            val adapter = ArrayAdapter<CharSequence>(
                context,
                android.R.layout.simple_spinner_item,
                entries
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun getSpinner(): Spinner {
        return spinner
    }

    fun showProgress() {
        cardView.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        cardView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }
}