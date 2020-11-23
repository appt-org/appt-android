package nl.appt.views.actions

import android.content.Context
import kotlinx.android.synthetic.main.action_selection.view.*
import nl.appt.R
import nl.appt.model.Action
import nl.appt.views.SelectionEditText

/**
 * Created by Jan Jaap de Groot on 23/11/2020
 * Copyright 2020 Stichting Appt
 */
class SelectionActionView(context: Context) : ActionView(
    context,
    Action.SELECTION,
    R.layout.action_selection
), SelectionEditText.OnSelectionChangedListener {

    init {
        textField.callback = this
    }

    override fun onSelectionChanged(start: Int, end: Int) {
        if ((end - start) > 0) {
            correct()
        }
    }
}