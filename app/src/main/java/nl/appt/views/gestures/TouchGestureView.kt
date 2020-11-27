package nl.appt.views.gestures

import android.content.Context
import android.view.MotionEvent
import nl.appt.extensions.isStart
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Gesture

/**
 * Created by Jan Jaap de Groot on 22/10/2020
 * Copyright 2020 Stichting Appt
 */
class TouchGestureView(
    context: Context,
    gesture: Gesture
): GestureView(gesture, context) {

    private var touched = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!touched && event?.isStart() == true) {
            touched = true
            correct()
        }
        return true
    }

    override fun onAccessibilityGesture(gesture: AccessibilityGesture) {
        incorrect("Je maakte een veegbeweging in plaats van een aanraking.")
    }
}