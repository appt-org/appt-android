package nl.appt.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import nl.appt.R
import nl.appt.extensions.getString
import nl.appt.extensions.identifier
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Topic : Item, Meer, Serializable {

    PROFILE,
    TERMS,
    PRIVACY,
    ACCESSIBILITY,
    SOURCE,
    CONTACT,
    SPONSOR;

    private fun getString(context: Context, property: String): String {
        return context.getString("topic_${identifier}_${property}")
    }

    override fun title(context: Context): String {
        return getString(context, "title")
    }

    override fun image(context: Context): Drawable? {
        return when (this) {
            PROFILE -> ContextCompat.getDrawable(context, R.drawable.icon_profile)
            TERMS -> ContextCompat.getDrawable(context, R.drawable.icon_terms)
            PRIVACY -> ContextCompat.getDrawable(context, R.drawable.icon_privacy)
            ACCESSIBILITY -> ContextCompat.getDrawable(context, R.drawable.icon_accessibility)
            SOURCE -> ContextCompat.getDrawable(context, R.drawable.icon_sourse)
            SPONSOR -> ContextCompat.getDrawable(context, R.drawable.icon_sponsor)
            CONTACT -> ContextCompat.getDrawable(context, R.drawable.icon_contact)
        }
    }

    val userProfile: Boolean
        get() {
            return when (this) {
                PROFILE -> true
                else -> false
            }
        }

    val slug: String?
        get() {
            return when (this) {
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
                CONTACT -> "https://appt.nl/contact"
                else -> null
            }
        }
}