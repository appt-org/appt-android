package nl.appt.views

import android.content.Context
import android.view.MotionEvent
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Direction
import nl.appt.model.Gesture

/**
 * Created by Jan Jaap de Groot on 15/10/2020
 * Copyright 2020 Stichting Appt
 */
class SwipeGestureView(
    context: Context,
    gesture: Gesture,
    private vararg val directions: Direction
): GestureView(gesture, context) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //Log.d(TAG, "onTouchEvent")

        swipeListener.onTouchEvent(event)

        return true
    }

    private val swipeListener = object : SwipeListener(context) {
        override fun onSwipe(directions: List<Direction>) {
            onDirections(directions.toTypedArray())
        }
    }

    private fun onDirections(directions: Array<Direction>) {
        if (directions.contentEquals(this.directions)) {
            correct()
        } else {
            incorrect("Veeg ${Direction.feedback(*this.directions)} in plaats van ${Direction.feedback(*directions)}")
        }
    }

    override fun onAccessibilityGesture(gesture: AccessibilityGesture) {
        onDirections(gesture.directions)
    }
}