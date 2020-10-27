package nl.appt.model

import android.text.Html
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 27/10/2020
 * Copyright 2020 Stichting Appt
 */
data class Content(
    val rendered: String
) : Serializable {

    private var _decoded: String? = null
    val decoded: String by lazy {
        var value = rendered

        _decoded?.let {
            value = it
        } ?: run {
            value = Html.fromHtml(value , Html.FROM_HTML_MODE_LEGACY).toString()
            _decoded = value
        }

        value
    }

    override fun toString(): String {
        return rendered
    }
}