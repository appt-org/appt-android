package nl.appt.model

import android.content.Context
import nl.appt.views.GestureView
import nl.appt.views.SwipeGestureView

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Gesture: Item {

    SWIPE_RIGHT,
    SWIPE_LEFT,
    SWIPE_UP,
    SWIPE_DOWN,

    SWIPE_UP_DOWN,
    SWIPE_DOWN_UP,
    SWIPE_RIGHT_LEFT,
    SWIPE_LEFT_RIGHT;

    override val title: String
        get() {
            return when (this) {
                SWIPE_RIGHT -> "Naar het volgende onderdeel gaan"
                SWIPE_LEFT -> "Naar het vorige onderdeel gaan"
                SWIPE_UP -> "Navigatie instelling omhoog aanpassen"
                SWIPE_DOWN -> "Navigatie instelling omlaag aanpassen"

                SWIPE_DOWN_UP -> "Naar het laatste onderdeel gaan"
                SWIPE_UP_DOWN -> "Naar het eerste onderdeel gaan"
                SWIPE_RIGHT_LEFT -> "Vooruit scrollen"
                SWIPE_LEFT_RIGHT -> "Terug scrollen"
            }
        }

    val description: String
        get() {
            return when (this) {
                SWIPE_RIGHT -> "Veeg naar rechts om naar het volgende onderdeel te gaan."
                SWIPE_LEFT -> "Veeg naar links om naar het vorige onderdeel te gaan."
                SWIPE_UP -> "Veeg omhoog om de navigatie instelling omhoog aan te passen."
                SWIPE_DOWN -> "Veeg omlaag om de navigatie instelling omlaag aan te passen."

                SWIPE_DOWN_UP -> "Veeg omlaag en dan omhoog om naar het laatste onderdeel te gaan."
                SWIPE_UP_DOWN -> "Veeg omhoog en dan omlaag om naar het eerste onderdeel te gaan."
                SWIPE_RIGHT_LEFT -> "Veeg naar rechts en dan naar links om vooruit te scrollen."
                SWIPE_LEFT_RIGHT -> "Veeg naar links en dan naar rechts om achteruit te scrollen."
            }
        }

    fun view(context: Context): GestureView {
        return when (this) {
            SWIPE_RIGHT -> SwipeGestureView(context, this, Direction.RIGHT)
            SWIPE_LEFT -> SwipeGestureView(context, this, Direction.LEFT)
            SWIPE_UP -> SwipeGestureView(context, this, Direction.UP)
            SWIPE_DOWN -> SwipeGestureView(context, this, Direction.DOWN)

            SWIPE_DOWN_UP -> SwipeGestureView(context, this, Direction.DOWN, Direction.UP)
            SWIPE_UP_DOWN -> SwipeGestureView(context, this, Direction.UP, Direction.DOWN)
            SWIPE_RIGHT_LEFT -> SwipeGestureView(context, this, Direction.RIGHT, Direction.LEFT)
            SWIPE_LEFT_RIGHT -> SwipeGestureView(context, this, Direction.LEFT, Direction.RIGHT)
        }
    }
}