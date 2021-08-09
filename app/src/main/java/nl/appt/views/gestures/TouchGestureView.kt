package nl.appt.views.gestures

import android.content.Context
import android.view.MotionEvent
import nl.appt.extensions.isStart
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
        super.onTouchEvent(event)

        if (!touched && event?.isStart() == true) {
            touched = true
            correct()
        }

        return true
    }

    override fun onAccessibilityGesture(gesture: Gesture) {
        if (gesture == gesture) {
            correct()
        } else {
            incorrect("Raak het scherm aan in plaats van te vegen.")
        }
    }
}