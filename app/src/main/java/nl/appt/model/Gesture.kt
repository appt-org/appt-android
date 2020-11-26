package nl.appt.model

import android.content.Context
import nl.appt.helpers.Preferences
import nl.appt.views.gestures.GestureView
import nl.appt.views.gestures.SwipeGestureView
import nl.appt.views.gestures.TapGestureView
import nl.appt.views.gestures.TouchGestureView
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Gesture: Training, Serializable {

    TOUCH,
    DOUBLE_TAP,

    SWIPE_RIGHT,
    SWIPE_LEFT,
    SWIPE_UP,
    SWIPE_DOWN,

    SWIPE_UP_DOWN,
    SWIPE_DOWN_UP,
    SWIPE_RIGHT_LEFT,
    SWIPE_LEFT_RIGHT,

    SWIPE_DOWN_LEFT,
    SWIPE_UP_LEFT,
    SWIPE_LEFT_UP,
    SWIPE_RIGHT_DOWN,
    SWIPE_LEFT_DOWN,
    SWIPE_UP_RIGHT,
    SWIPE_DOWN_RIGHT;

    override fun title(): String {
        return title
    }

    val title: String
        get() {
            return when (this) {
                TOUCH -> "Een onderdeel selecteren en uitspreken"
                DOUBLE_TAP -> "Het geselecteerde onderdeel activeren"

                SWIPE_RIGHT -> "Naar het volgende onderdeel gaan"
                SWIPE_LEFT -> "Naar het vorige onderdeel gaan"
                SWIPE_UP -> "Navigatie instelling omhoog aanpassen"
                SWIPE_DOWN -> "Navigatie instelling omlaag aanpassen"

                SWIPE_DOWN_UP -> "Naar het laatste onderdeel gaan"
                SWIPE_UP_DOWN -> "Naar het eerste onderdeel gaan"
                SWIPE_RIGHT_LEFT -> "Vooruit scrollen"
                SWIPE_LEFT_RIGHT -> "Terug scrollen"

                SWIPE_DOWN_LEFT -> "Terug"
                SWIPE_UP_LEFT -> "Thuis"
                SWIPE_LEFT_UP -> "Overzicht"
                SWIPE_RIGHT_DOWN -> "Meldingen"
                SWIPE_LEFT_DOWN -> "Zoeken"
                SWIPE_UP_RIGHT -> "Lokaal contextmenu"
                SWIPE_DOWN_RIGHT -> "Algemeen contextmenu"
            }
        }

    val description: String
        get() {
            return when (this) {
                TOUCH -> "Raak het scherm aan om een onderdeel te selecteren en uit te spreken."
                DOUBLE_TAP -> "Dubbeltik om het geselecteerde onderdeel te activeren."

                SWIPE_RIGHT -> "Veeg naar rechts om naar het volgende onderdeel te gaan."
                SWIPE_LEFT -> "Veeg naar links om naar het vorige onderdeel te gaan."
                SWIPE_UP -> "Veeg omhoog om de navigatie instelling omhoog aan te passen."
                SWIPE_DOWN -> "Veeg omlaag om de navigatie instelling omlaag aan te passen."

                SWIPE_DOWN_UP -> "Veeg omlaag en dan omhoog om naar het laatste onderdeel te gaan."
                SWIPE_UP_DOWN -> "Veeg omhoog en dan omlaag om naar het eerste onderdeel te gaan."
                SWIPE_RIGHT_LEFT -> "Veeg naar rechts en dan naar links om vooruit te scrollen."
                SWIPE_LEFT_RIGHT -> "Veeg naar links en dan naar rechts om achteruit te scrollen."

                SWIPE_DOWN_LEFT -> "Veeg omlaag en dan naar links om terug te gaan "
                SWIPE_UP_LEFT -> "Veeg omhoog en dan naar links om de thuisknop te activeren"
                SWIPE_LEFT_UP -> "Veeg naar links en dan omhoog om recente apps te bekijken"
                SWIPE_RIGHT_DOWN -> "Veeg naar rechts en dan omlaag om meldingen te tonen"
                SWIPE_LEFT_DOWN -> "Veeg naar links en dan omlaag om op het scherm te zoeken"
                SWIPE_UP_RIGHT -> "Veeg omhoog en dan naar rechts om het lokale contextmenu te openen"
                SWIPE_DOWN_RIGHT -> "Veeg omlaag en dan naar rechts om het algemene contextmenu te openen"
            }
        }

    fun view(context: Context): GestureView {
        return when (this) {
            TOUCH -> TouchGestureView(context, this)
            DOUBLE_TAP -> TapGestureView(context, this, 1, 2)

            SWIPE_RIGHT -> SwipeGestureView(context, this, Direction.RIGHT)
            SWIPE_LEFT -> SwipeGestureView(context, this, Direction.LEFT)
            SWIPE_UP -> SwipeGestureView(context, this, Direction.UP)
            SWIPE_DOWN -> SwipeGestureView(context, this, Direction.DOWN)

            SWIPE_DOWN_UP -> SwipeGestureView(context, this, Direction.DOWN, Direction.UP)
            SWIPE_UP_DOWN -> SwipeGestureView(context, this, Direction.UP, Direction.DOWN)
            SWIPE_RIGHT_LEFT -> SwipeGestureView(context, this, Direction.RIGHT, Direction.LEFT)
            SWIPE_LEFT_RIGHT -> SwipeGestureView(context, this, Direction.LEFT, Direction.RIGHT)

            SWIPE_DOWN_LEFT -> SwipeGestureView(context, this, Direction.DOWN, Direction.LEFT)
            SWIPE_UP_LEFT -> SwipeGestureView(context, this, Direction.UP, Direction.LEFT)
            SWIPE_LEFT_UP -> SwipeGestureView(context, this, Direction.LEFT, Direction.UP)
            SWIPE_RIGHT_DOWN -> SwipeGestureView(context, this, Direction.RIGHT, Direction.DOWN)
            SWIPE_LEFT_DOWN -> SwipeGestureView(context, this, Direction.LEFT, Direction.DOWN)
            SWIPE_UP_RIGHT -> SwipeGestureView(context, this, Direction.UP, Direction.RIGHT)
            SWIPE_DOWN_RIGHT -> SwipeGestureView(context, this, Direction.DOWN, Direction.RIGHT)
        }
    }

    override fun completed(context: Context): Boolean {
        return Preferences(context).isCompleted(this)
    }
    override fun completed(context: Context, completed: Boolean) {
        Preferences(context).setCompleted(this, true)
    }
}