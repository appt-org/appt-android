package nl.appt.model

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

    override fun title(): String {
        return title
    }

    val title: String
        get() {
            return when (this) {
                TERMS -> "Algemene voorwaarden"
                PRIVACY -> "Privacybeleid"
                ACCESSIBILITY -> "Toegankelijkheidsverklaring"

                SOURCE -> "Bekijk de broncode"
                SPONSOR -> "Ondersteund door het SIDN fonds"
            }
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