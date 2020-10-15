package nl.appt.views

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import nl.appt.model.Direction

/**
 * Created by Jan Jaap de Groot on 15/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class SwipeListener(val context: Context) {

    private val THRESHOLD = 25
    private var directions = arrayListOf<Direction>()

    abstract fun onSwipe(directions: List<Direction>)

    fun onTouchEvent(event: MotionEvent?) {
        gestureDetector.onTouchEvent(event)
    }

    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            // Determine direction
            var direction = Direction.UNKNOWN
            if (distanceX > THRESHOLD) {
                if (distanceY > THRESHOLD) {
                    direction = Direction.TOP_LEFT
                } else if (distanceY < -THRESHOLD) {
                    direction = Direction.BOTTOM_LEFT
                } else {
                    direction = Direction.LEFT
                }
            } else if (distanceX < -THRESHOLD) {
                if (distanceY > THRESHOLD) {
                    direction = Direction.TOP_RIGHT
                } else if (distanceY < -THRESHOLD) {
                    direction = Direction.BOTTOM_RIGHT
                } else {
                    direction = Direction.RIGHT
                }
            } else {
                if (distanceY > THRESHOLD) {
                    direction = Direction.UP
                } else if (distanceY < -THRESHOLD) {
                    direction = Direction.DOWN
                }
            }

            if (direction != Direction.UNKNOWN) {
                // Set amount of fingers
                direction.fingers = e2?.pointerCount ?: 1

                if (directions.isEmpty()) {
                    // Add first direction
                    directions.add(direction)
                } else {
                    // Only add if direction is different than last direction
                    directions.lastOrNull()?.let { lastDirection ->
                        if (direction != lastDirection) {
                            directions.add(direction)
                        }
                    }
                }
            }

            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            onSwipe(directions)
            directions.clear()

            return super.onFling(e1, e2, velocityX, velocityY)
        }
    })
}