package nl.appt.model

import android.accessibilityservice.AccessibilityService

/**
 * Created by Jan Jaap de Groot on 15/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class AccessibilityGesture(val gestureId: Int) {

    // UP
    SWIPE_UP(AccessibilityService.GESTURE_SWIPE_UP),
    SWIPE_UP_AND_RIGHT(AccessibilityService.GESTURE_SWIPE_UP_AND_RIGHT),
    SWIPE_UP_AND_DOWN(AccessibilityService.GESTURE_SWIPE_UP_AND_DOWN),
    SWIPE_UP_AND_LEFT(AccessibilityService.GESTURE_SWIPE_UP_AND_LEFT),

    // RIGHT
    SWIPE_RIGHT(AccessibilityService.GESTURE_SWIPE_RIGHT),
    SWIPE_RIGHT_AND_UP(AccessibilityService.GESTURE_SWIPE_RIGHT_AND_UP),
    SWIPE_RIGHT_AND_DOWN(AccessibilityService.GESTURE_SWIPE_RIGHT_AND_DOWN),
    SWIPE_RIGHT_AND_LEFT(AccessibilityService.GESTURE_SWIPE_RIGHT_AND_LEFT),

    // DOWN
    SWIPE_DOWN(AccessibilityService.GESTURE_SWIPE_DOWN),
    SWIPE_DOWN_AND_UP(AccessibilityService.GESTURE_SWIPE_DOWN_AND_UP),
    SWIPE_DOWN_AND_RIGHT(AccessibilityService.GESTURE_SWIPE_DOWN_AND_RIGHT),
    SWIPE_DOWN_AND_LEFT(AccessibilityService.GESTURE_SWIPE_DOWN_AND_LEFT),

    // LEFT
    SWIPE_LEFT(AccessibilityService.GESTURE_SWIPE_LEFT),
    SWIPE_LEFT_AND_UP(AccessibilityService.GESTURE_SWIPE_LEFT_AND_UP),
    SWIPE_LEFT_AND_RIGHT(AccessibilityService.GESTURE_SWIPE_LEFT_AND_RIGHT),
    SWIPE_LEFT_AND_DOWN(AccessibilityService.GESTURE_SWIPE_LEFT_AND_DOWN);

    val directions: Array<Direction>
        get() {
            return when (this) {
                SWIPE_UP -> arrayOf(Direction.UP)
                SWIPE_UP_AND_RIGHT -> arrayOf(Direction.UP, Direction.RIGHT)
                SWIPE_UP_AND_DOWN -> arrayOf(Direction.UP, Direction.DOWN)
                SWIPE_UP_AND_LEFT -> arrayOf(Direction.UP, Direction.LEFT)

                SWIPE_RIGHT -> arrayOf(Direction.RIGHT)
                SWIPE_RIGHT_AND_UP -> arrayOf(Direction.RIGHT, Direction.UP)
                SWIPE_RIGHT_AND_DOWN -> arrayOf(Direction.RIGHT, Direction.DOWN)
                SWIPE_RIGHT_AND_LEFT -> arrayOf(Direction.RIGHT, Direction.LEFT)

                SWIPE_DOWN -> arrayOf(Direction.DOWN)
                SWIPE_DOWN_AND_UP -> arrayOf(Direction.DOWN, Direction.UP)
                SWIPE_DOWN_AND_RIGHT -> arrayOf(Direction.DOWN, Direction.RIGHT)
                SWIPE_DOWN_AND_LEFT -> arrayOf(Direction.DOWN, Direction.LEFT)

                SWIPE_LEFT -> arrayOf(Direction.LEFT)
                SWIPE_LEFT_AND_UP -> arrayOf(Direction.LEFT, Direction.UP)
                SWIPE_LEFT_AND_RIGHT -> arrayOf(Direction.LEFT, Direction.RIGHT)
                SWIPE_LEFT_AND_DOWN -> arrayOf(Direction.LEFT, Direction.DOWN)
            }
        }

    companion object {
        fun from(gestureId: Int) = values().firstOrNull {
            it.gestureId == gestureId
        }
    }
}