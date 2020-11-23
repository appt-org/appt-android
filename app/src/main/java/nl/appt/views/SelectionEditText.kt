package nl.appt.views

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

/**
 * Created by Jan Jaap de Groot on 23/11/2020
 * Copyright 2020 Stichting Appt
 */

class SelectionEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : EditText(context, attrs, defStyle) {

    interface OnSelectionChangedListener {
        fun onSelectionChanged(start: Int, end: Int)
    }

    var callback: OnSelectionChangedListener? = null

    override fun onSelectionChanged(start: Int, end: Int) {
        super.onSelectionChanged(start, end)
        callback?.onSelectionChanged(start, end)
    }
}