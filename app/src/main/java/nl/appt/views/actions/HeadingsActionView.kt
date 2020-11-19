package nl.appt.views.actions

import android.content.Context
import android.util.Log
import nl.appt.R
import nl.appt.extensions.className
import nl.appt.model.Action

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class HeadingsActionView(context: Context) : ActionView(context, Action.HEADINGS, R.layout.action_headings) {

    override fun onFocusChanged(elements: List<String>) {
        if (elements.size >= HEADING_COUNT) {
            if (elements.takeLast(HEADING_COUNT).all { className ->
                className  == HEADING_CLASS_NAME
            }) {
                correct()
            }
        }
    }

    companion object {
        private const val HEADING_COUNT = 3
        private val HEADING_CLASS_NAME = className<HeadingTextView>()
    }
}