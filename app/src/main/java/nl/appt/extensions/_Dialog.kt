package nl.appt.extensions

import androidx.appcompat.app.AlertDialog
import nl.appt.model.Gesture

/**
 * Created by Jan Jaap de Groot on 07/12/2020
 * Copyright 2020 Stichting Appt
 */
fun AlertDialog.onAccessibilityGesture(gesture: Gesture) {
    if (gesture == Gesture.ONE_FINGER_SWIPE_LEFT) {
        getButton(AlertDialog.BUTTON_NEGATIVE)?.performClick()
    } else if (gesture == Gesture.ONE_FINGER_SWIPE_RIGHT) {
        getButton(AlertDialog.BUTTON_POSITIVE)?.performClick()
    }
}