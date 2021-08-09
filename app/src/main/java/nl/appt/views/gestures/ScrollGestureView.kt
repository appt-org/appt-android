package nl.appt.views.gestures

import android.content.Context
import android.util.Log
import nl.appt.model.Direction
import nl.appt.model.Gesture

/**
 * Created by Jan Jaap de Groot on 15/10/2020
 * Copyright 2020 Stichting Appt
 */
class ScrollGestureView(
    context: Context,
    gesture: Gesture
): SwipeGestureView(context, gesture) {

    private val TAG = "ScrollGestureView"
    private val requiredFingers = 2

    override fun onSwipe(directions: Array<Direction>) {
        swiped = true

        val usedFingers = directions.map { it.fingers }.average().toInt()
        Log.d(TAG, "onSwipe: ${directions.joinToString { it.toString() }}, usedFingers: $usedFingers")

        if (requiredFingers != usedFingers) {
            incorrect("Gebruik $requiredFingers vingers in plaats van $usedFingers vingers.")
        } else if (directions.contentEquals(gesture.directions)) {
            correct()
        } else {
            incorrect("Je veegde ${Direction.feedback(directions)}.")
        }
    }
}