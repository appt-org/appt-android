package nl.appt.model

import android.content.Context
import nl.appt.extensions.getString
import nl.appt.extensions.identifier
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Topic: Item, Serializable {

    TERMS,
    PRIVACY,
    ACCESSIBILITY,

    SOURCE,
    SPONSOR;

    private fun getString(context: Context, property: String): String {
        return context.getString("topic_${identifier}_${property}")
    }

    override fun title(context: Context): String {
        return getString(context, "title")
    }

    val slug: String?
        get() {
            return when(this) {
                TERMS -> "algemene-voorwaarden"
                PRIVACY -> "privacybeleid"
                ACCESSIBILITY -> "toegankelijkheidsverklaring"
                else -> null
            }
        }

    val url: String?
        get() {
            return when (this) {
                SOURCE -> "https://github.com/appt-nl/appt-android"
                SPONSOR -> "https://www.sidnfonds.nl"
                else -> null
            }
        }
}