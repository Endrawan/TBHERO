package com.tbhero.application.components

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import com.tbhero.application.R.layout.view_edit_text
import com.tbhero.application.R.styleable.*
import kotlinx.android.synthetic.main.view_edit_text.view.*
import java.text.SimpleDateFormat
import java.util.*


class EditTextView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var timeMills: Long? = null
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
            2 -> editText.inputType = InputType.TYPE_CLASS_PHONE or InputType.TYPE_TEXT_VARIATION_PHONETIC
            3 -> editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            4 -> editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            5 -> {
                editText.inputType = InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE
                val calendar = Calendar.getInstance()
                val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateLabel(calendar)
                    timeMills = calendar.time.time
                }
                editText.isFocusable = false
                editText.isFocusableInTouchMode = false
                editText.isClickable = true
                editText.setOnClickListener {
                    DatePickerDialog(
                        context, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
                            Calendar.DAY_OF_MONTH
                        )
                    ).show()
                }
            }
        }
    }

    private fun updateLabel(calendar: Calendar) {
        val format = "dd MMMM yyyy"
        val locale = Locale("in", "ID")
        val sdf = SimpleDateFormat(format, locale)
        editText.setText(sdf.format(calendar.time))
    }

    fun getEditText(): EditText {
        return editText
    }

    fun getTimeMills(): Long? {
        return timeMills
    }

    fun setTimeMills(mills: Long?) {
        val calendar = Calendar.getInstance()
        if (mills != null) {
            calendar.timeInMillis = mills
            updateLabel(calendar)
        }
    }
}