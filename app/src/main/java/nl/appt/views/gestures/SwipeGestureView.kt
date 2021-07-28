package nl.appt.views.gestures

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.extensions.isEnd
import nl.appt.extensions.isStart
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Direction
import nl.appt.model.Gesture

/**
 * Created by Jan Jaap de Groot on 15/10/2020
 * Copyright 2020 Stichting Appt
 */
open class SwipeGestureView(
    context: Context,
    gesture: Gesture,
    vararg val directions: Direction
): GestureView(gesture, context) {

    var swiped = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        if (event?.isStart() == true) {
            swiped = false
        } else if (event?.isEnd() == true) {
            if (!swiped) {
                incorrect("Je maakte geen veegbeweging.")
            }
        }

        return true
    }

    override fun onAccessibilityGesture(gesture: AccessibilityGesture) {
        onSwipe(gesture.directions)
    }

    open fun onSwipe(directions: Array<Direction>) {
        swiped = true

        if (directions.contentEquals(this.directions)) {
            correct()
        } else {
            incorrect("Je veegde ${Direction.feedback(*directions)}.")
        }
    }

    override fun incorrect(feedback: String) {
        val instructions = "Veeg ${Direction.feedback(*this.directions)}."
        super.incorrect("$feedback $instructions")
    }

    /**
     * Custom implementation of GestureDetector to detect swipe directions
     */
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

        private val THRESHOLD = 40
        private var path = arrayListOf<Direction>()

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            // Determine direction
            var direction = Direction.UNKNOWN
            when {
                distanceX > THRESHOLD -> {
                    direction = Direction.LEFT
                }
                distanceX < -THRESHOLD -> {
                    direction = Direction.RIGHT
                }
                distanceY > THRESHOLD -> {
                    direction = Direction.UP
                }
                distanceY < -THRESHOLD -> {
                    direction = Direction.DOWN
                }
            }

            if (direction != Direction.UNKNOWN) {
                // Set amount of fingers
                direction.fingers = e2?.pointerCount ?: 1

                // Add one finger if TalkBack is activated
                if (Accessibility.isTalkBackEnabled(context)) {
                    if (direction.fingers == 1) {
                        direction.fingers = 2
                    }
                }

                if (path.isEmpty()) {
                    // Add first direction
                    path.add(direction)
                    Log.d("Swipe", "Direction: $direction, fingers: ${direction.fingers}")
                } else {
                    // Only add if direction is different than last direction
                    path.lastOrNull()?.let { lastDirection ->
                        if (direction != lastDirection) {
                            path.add(direction)
                            Log.d("Swipe", "Direction: $direction, fingers: ${direction.fingers}")
                        }
                    }
                }
            }

            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (path.isNotEmpty()) {
                onSwipe(path.toTypedArray())
            }
            path.clear()

            return super.onFling(e1, e2, velocityX, velocityY)
        }
    })
}