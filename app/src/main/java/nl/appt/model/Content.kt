package nl.appt.model

import android.text.Html
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 27/10/2020
 * Copyright 2020 Stichting Appt
 */
class Content(
    val rendered: String
) : Serializable {

    fun decoded(): String {
        return Html.fromHtml(rendered, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    override fun toString(): String {
        return rendered
    }
}