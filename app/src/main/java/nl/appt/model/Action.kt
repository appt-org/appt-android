package nl.appt.model

import android.content.Context
import nl.appt.R
import nl.appt.extensions.getString
import nl.appt.extensions.identifier
import nl.appt.helpers.Preferences
import nl.appt.views.actions.*
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 06/11/2020
 * Copyright 2020 Stichting Appt
 */
enum class Action: Training, Serializable {

    HEADINGS,
    LINKS,
    SELECTION,
    COPY,
    PASTE;

    private fun getString(context: Context, property: String): String {
        return context.getString("talkback_action_${identifier}_${property}")
    }

    override fun title(context: Context): String {
        return getString(context, "title")
    }

    fun view(context: Context): ActionView {
        return when (this) {
            HEADINGS -> HeadingsActionView(context)
            LINKS -> LinksActionView(context)
            SELECTION -> SelectionActionView(context)
            COPY -> CopyActionView(context)
            PASTE -> PasteActionView(context)
        }
    }

    override fun completed(context: Context): Boolean {
        return Preferences(context).isCompleted(this)
    }
    override fun completed(context: Context, completed: Boolean) {
        Preferences(context).setCompleted(this, true)
    }
}