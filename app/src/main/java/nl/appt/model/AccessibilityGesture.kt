package nl.appt.model

import android.accessibilityservice.AccessibilityService
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Created by Jan Jaap de Groot on 15/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class AccessibilityGesture(val gestureId: Int) {

    // ONE FINGER SWIPE UP
    SWIPE_UP(AccessibilityService.GESTURE_SWIPE_UP),
    SWIPE_UP_AND_RIGHT(AccessibilityService.GESTURE_SWIPE_UP_AND_RIGHT),
    SWIPE_UP_AND_DOWN(AccessibilityService.GESTURE_SWIPE_UP_AND_DOWN),
    SWIPE_UP_AND_LEFT(AccessibilityService.GESTURE_SWIPE_UP_AND_LEFT),

    // ONE FINGER SWIPE RIGHT
    SWIPE_RIGHT(AccessibilityService.GESTURE_SWIPE_RIGHT),
    SWIPE_RIGHT_AND_UP(AccessibilityService.GESTURE_SWIPE_RIGHT_AND_UP),
    SWIPE_RIGHT_AND_DOWN(AccessibilityService.GESTURE_SWIPE_RIGHT_AND_DOWN),
    SWIPE_RIGHT_AND_LEFT(AccessibilityService.GESTURE_SWIPE_RIGHT_AND_LEFT),

    // ONE FINGER SWIPE DOWN
    SWIPE_DOWN(AccessibilityService.GESTURE_SWIPE_DOWN),
    SWIPE_DOWN_AND_UP(AccessibilityService.GESTURE_SWIPE_DOWN_AND_UP),
    SWIPE_DOWN_AND_RIGHT(AccessibilityService.GESTURE_SWIPE_DOWN_AND_RIGHT),
    SWIPE_DOWN_AND_LEFT(AccessibilityService.GESTURE_SWIPE_DOWN_AND_LEFT),

    // ONE FINGER SWIPE LEFT
    SWIPE_LEFT(AccessibilityService.GESTURE_SWIPE_LEFT),
    SWIPE_LEFT_AND_UP(AccessibilityService.GESTURE_SWIPE_LEFT_AND_UP),
    SWIPE_LEFT_AND_RIGHT(AccessibilityService.GESTURE_SWIPE_LEFT_AND_RIGHT),
    SWIPE_LEFT_AND_DOWN(AccessibilityService.GESTURE_SWIPE_LEFT_AND_DOWN),

    // ONE FINGER TAP
    @RequiresApi(Build.VERSION_CODES.R)
    ONE_FINGER_DOUBLE_TAP(AccessibilityService.GESTURE_DOUBLE_TAP),
    @RequiresApi(Build.VERSION_CODES.R)
    ONE_FINGER_DOUBLE_TAP_HOLD(AccessibilityService.GESTURE_DOUBLE_TAP_AND_HOLD),

    // TWO FINGER TAP
    @RequiresApi(Build.VERSION_CODES.R)
    TWO_FINGER_SINGLE_TAP(AccessibilityService.GESTURE_2_FINGER_SINGLE_TAP),
    @RequiresApi(Build.VERSION_CODES.R)
    TWO_FINGER_DOUBLE_TAP(AccessibilityService.GESTURE_2_FINGER_DOUBLE_TAP),
    @RequiresApi(Build.VERSION_CODES.R)
    TWO_FINGER_DOUBLE_TAP_HOLD(AccessibilityService.GESTURE_2_FINGER_DOUBLE_TAP_AND_HOLD),
    @RequiresApi(Build.VERSION_CODES.R)
    TWO_FINGER_TRIPLE_TAP(AccessibilityService.GESTURE_2_FINGER_TRIPLE_TAP),

    // TWO FINGER SWIPE
    @RequiresApi(Build.VERSION_CODES.R)
    TWO_FINGER_SWIPE_UP(AccessibilityService.GESTURE_2_FINGER_SWIPE_UP),
    @RequiresApi(Build.VERSION_CODES.R)
    TWO_FINGER_SWIPE_RIGHT(AccessibilityService.GESTURE_2_FINGER_SWIPE_RIGHT),
    @RequiresApi(Build.VERSION_CODES.R)
    TWO_FINGER_SWIPE_DOWN(AccessibilityService.GESTURE_2_FINGER_SWIPE_DOWN),
    @RequiresApi(Build.VERSION_CODES.R)
    TWO_FINGER_SWIPE_LEFT(AccessibilityService.GESTURE_2_FINGER_SWIPE_LEFT),

    // THREE FINGER TAP
    @RequiresApi(Build.VERSION_CODES.R)
    THREE_FINGER_SINGLE_TAP(AccessibilityService.GESTURE_3_FINGER_SINGLE_TAP),
    @RequiresApi(Build.VERSION_CODES.R)
    THREE_FINGER_DOUBLE_TAP(AccessibilityService.GESTURE_3_FINGER_DOUBLE_TAP),
    @RequiresApi(Build.VERSION_CODES.R)
    THREE_FINGER_DOUBLE_TAP_HOLD(AccessibilityService.GESTURE_3_FINGER_DOUBLE_TAP_AND_HOLD),
    @RequiresApi(Build.VERSION_CODES.R)
    THREE_FINGER_TRIPLE_TAP(AccessibilityService.GESTURE_3_FINGER_TRIPLE_TAP),

    // THREE FINGER SWIPE
    @RequiresApi(Build.VERSION_CODES.R)
    THREE_FINGER_SWIPE_UP(AccessibilityService.GESTURE_3_FINGER_SWIPE_UP),
    @RequiresApi(Build.VERSION_CODES.R)
    THREE_FINGER_SWIPE_RIGHT(AccessibilityService.GESTURE_3_FINGER_SWIPE_RIGHT),
    @RequiresApi(Build.VERSION_CODES.R)
    THREE_FINGER_SWIPE_DOWN(AccessibilityService.GESTURE_3_FINGER_SWIPE_DOWN),
    @RequiresApi(Build.VERSION_CODES.R)
    THREE_FINGER_SWIPE_LEFT(AccessibilityService.GESTURE_3_FINGER_SWIPE_LEFT),

    // FOUR FINGER TAP
    @RequiresApi(Build.VERSION_CODES.R)
    FOUR_FINGER_SINGLE_TAP(AccessibilityService.GESTURE_4_FINGER_SINGLE_TAP),
    @RequiresApi(Build.VERSION_CODES.R)
    FOUR_FINGER_DOUBLE_TAP(AccessibilityService.GESTURE_4_FINGER_DOUBLE_TAP),
    @RequiresApi(Build.VERSION_CODES.R)
    FOUR_FINGER_DOUBLE_TAP_HOLD(AccessibilityService.GESTURE_4_FINGER_DOUBLE_TAP_AND_HOLD),
    @RequiresApi(Build.VERSION_CODES.R)
    FOUR_FINGER_TRIPLE_TAP(AccessibilityService.GESTURE_4_FINGER_TRIPLE_TAP),

    // FOUR FINGER SWIPE
    @RequiresApi(Build.VERSION_CODES.R)
    FOUR_FINGER_SWIPE_UP(AccessibilityService.GESTURE_4_FINGER_SWIPE_UP),
    @RequiresApi(Build.VERSION_CODES.R)
    FOUR_FINGER_SWIPE_RIGHT(AccessibilityService.GESTURE_4_FINGER_SWIPE_RIGHT),
    @RequiresApi(Build.VERSION_CODES.R)
    FOUR_FINGER_SWIPE_DOWN(AccessibilityService.GESTURE_4_FINGER_SWIPE_DOWN),
    @RequiresApi(Build.VERSION_CODES.R)
    FOUR_FINGER_SWIPE_LEFT(AccessibilityService.GESTURE_4_FINGER_SWIPE_LEFT);

    val directions: Array<Direction>
        get() {
            val array = when (this) {
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

                else -> arrayOf()
            }

            array.forEach { direction ->
                direction.fingers = 1
            }

            return array
        }

    companion object {
        fun from(gestureId: Int) = values().firstOrNull { gesture ->
            gesture.gestureId == gestureId
        }
    }
}