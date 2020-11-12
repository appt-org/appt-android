package nl.appt.model

import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 06/11/2020
 * Copyright 2020 Stichting Appt
 */
enum class Course: Item, Serializable {

    TALKBACK_GESTURES,
    TALKBACK_ENABLE,
    TALKBACK_ACTIONS;

    override fun title(): String {
        return title
    }

    val title: String
        get() {
            return when (this) {
                TALKBACK_GESTURES -> "TalkBack gebaren"
                TALKBACK_ENABLE -> "TalkBack aanzetten"
                TALKBACK_ACTIONS -> "TalkBack handelingen"
            }
        }
}