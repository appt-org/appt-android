package nl.appt.model

import android.accessibilityservice.AccessibilityService

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Gesture(val gestureId: Int? = null) {

    //TOUCH(-1),
    SWIPE_RIGHT(AccessibilityService.GESTURE_SWIPE_RIGHT),
    SWIPE_LEFT(AccessibilityService.GESTURE_SWIPE_LEFT),
    //DOUBLE_TAP(-1),

    SWIPE_UP_AND_DOWN(AccessibilityService.GESTURE_SWIPE_UP_AND_DOWN),
    SWIPE_DOWN_AND_UP(AccessibilityService.GESTURE_SWIPE_DOWN_AND_UP);

//    fun title(): String {
//        return "Test"
//    }

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
                SWIPE_LEFT -> "Veeg nar links om naar het vorige onderdeel."
                SWIPE_DOWN_AND_UP -> "Veeg omlaag en dan omhoog om naar het laatste onderdeel te gaan."
                SWIPE_UP_AND_DOWN -> "Veeg omhoog en dan omlag om naar het het eerste onderdeel te gaan."
            }
        }
}