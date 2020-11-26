package nl.appt.model

import android.content.Context
import nl.appt.R
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

    override fun title(): String {
        return title
    }

    val title: String
        get() {
            return when (this) {
                HEADINGS -> "Navigeren via koppen"
                LINKS -> "Navigeren via links"
                SELECTION -> "Tekst selecteren"
                COPY -> "Kopiëren"
                PASTE -> "Plakken"
            }
        }

    val layoutId: Int
        get() {
            return R.layout.action_headings
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