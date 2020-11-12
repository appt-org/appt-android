package nl.appt.model

import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 06/11/2020
 * Copyright 2020 Stichting Appt
 */
enum class Action: Item, Serializable {

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
}