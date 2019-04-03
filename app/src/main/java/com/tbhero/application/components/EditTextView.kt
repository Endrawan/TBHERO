package com.tbhero.application.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tbhero.application.R.layout.view_edit_text
import com.tbhero.application.R.styleable.*
import kotlinx.android.synthetic.main.view_edit_text.view.*

class EditTextView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            EditTextView, 0, 0
        )
        val hint = a.getString(EditTextView_editTextHint)
        val drawableLeft = a.getDrawable(EditTextView_editTextDrawableLeft)
        val inputType = a.getInt(EditTextView_editTextInputType, 0)
        a.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(view_edit_text, this, true)

        inputLayout.hint = hint
        editText.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
        when (inputType) {
            0 -> editText.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            1 -> editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            2 -> editText.inputType = InputType.TYPE_TEXT_VARIATION_PHONETIC
            3 -> editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            4 -> editText.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
            5 -> editText.inputType = InputType.TYPE_DATETIME_VARIATION_DATE
        }

    }
}