package nl.appt.model

import android.accessibilityservice.AccessibilityService
import android.content.Context
import nl.appt.views.GestureView
import nl.appt.views.SwipeGestureView

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Gesture {

    SWIPE_RIGHT,
    SWIPE_LEFT,

    SWIPE_UP_AND_DOWN,
    SWIPE_DOWN_AND_UP;

    val title: String
        get() {
            return when (this) {
                SWIPE_RIGHT -> "Naar het volgende onderdeel"
                SWIPE_LEFT -> "Naar het vorige onderdeel"
                SWIPE_DOWN_AND_UP -> "Naar het laatste onderdeel"
                SWIPE_UP_AND_DOWN -> "Naar het eerste onderdeel"
            }
        }

    val description: String
        get() {
            return when (this) {
                SWIPE_RIGHT -> "Veeg naar rechts om naar het volgende onderdeel te gaan."
                SWIPE_LEFT -> "Veeg naar links om naar het vorige onderdeel."
                SWIPE_DOWN_AND_UP -> "Veeg omlaag en dan omhoog om naar het laatste onderdeel te gaan."
                SWIPE_UP_AND_DOWN -> "Veeg omhoog en dan omlaag om naar het het eerste onderdeel te gaan."
            }
        }

    fun view(context: Context): GestureView {
        return when (this) {
            SWIPE_RIGHT -> SwipeGestureView(context, this, Direction.RIGHT)
            SWIPE_LEFT -> SwipeGestureView(context, this, Direction.LEFT)
            SWIPE_DOWN_AND_UP -> SwipeGestureView(context, this, Direction.DOWN, Direction.UP)
            SWIPE_UP_AND_DOWN -> SwipeGestureView(context, this, Direction.UP, Direction.DOWN)
        }
    }
}