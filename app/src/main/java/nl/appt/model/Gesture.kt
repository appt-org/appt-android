package nl.appt.model

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.os.Build
import nl.appt.extensions.getString
import nl.appt.extensions.identifier
import nl.appt.helpers.Preferences
import nl.appt.views.gestures.*
import java.io.Serializable
import kotlin.collections.ArrayList

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Gesture: Training, Serializable {

    ONE_FINGER_TOUCH,
    ONE_FINGER_DOUBLE_TAP,
    ONE_FINGER_DOUBLE_TAP_HOLD,

    ONE_FINGER_SWIPE_RIGHT,
    ONE_FINGER_SWIPE_LEFT,
    ONE_FINGER_SWIPE_UP,
    ONE_FINGER_SWIPE_DOWN,

    TWO_FINGER_SWIPE_DOWN,
    TWO_FINGER_SWIPE_UP,
    TWO_FINGER_SWIPE_RIGHT,
    TWO_FINGER_SWIPE_LEFT,

    ONE_FINGER_SWIPE_UP_THEN_RIGHT,
    ONE_FINGER_SWIPE_UP_THEN_DOWN,
    ONE_FINGER_SWIPE_UP_THEN_LEFT,

    ONE_FINGER_SWIPE_DOWN_THEN_UP,
    ONE_FINGER_SWIPE_DOWN_THEN_RIGHT,
    ONE_FINGER_SWIPE_DOWN_THEN_LEFT,

    ONE_FINGER_SWIPE_RIGHT_THEN_DOWN,
    ONE_FINGER_SWIPE_RIGHT_THEN_LEFT,

    ONE_FINGER_SWIPE_LEFT_THEN_UP,
    ONE_FINGER_SWIPE_LEFT_THEN_RIGHT,
    ONE_FINGER_SWIPE_LEFT_THEN_DOWN;

    val directions: Array<Direction>
        get() {
            val array = when (this) {
                ONE_FINGER_SWIPE_UP -> arrayOf(Direction.UP)
                ONE_FINGER_SWIPE_RIGHT -> arrayOf(Direction.RIGHT)
                ONE_FINGER_SWIPE_DOWN -> arrayOf(Direction.DOWN)
                ONE_FINGER_SWIPE_LEFT -> arrayOf(Direction.LEFT)

                TWO_FINGER_SWIPE_UP -> arrayOf(Direction.UP)
                TWO_FINGER_SWIPE_RIGHT -> arrayOf(Direction.RIGHT)
                TWO_FINGER_SWIPE_DOWN -> arrayOf(Direction.DOWN)
                TWO_FINGER_SWIPE_LEFT -> arrayOf(Direction.LEFT)

                ONE_FINGER_SWIPE_UP_THEN_RIGHT -> arrayOf(Direction.UP, Direction.RIGHT)
                ONE_FINGER_SWIPE_UP_THEN_DOWN -> arrayOf(Direction.UP, Direction.DOWN)
                ONE_FINGER_SWIPE_UP_THEN_LEFT -> arrayOf(Direction.UP, Direction.LEFT)

                //ONE_FINGER_SWIPE_RIGHT_THEN_UP -> arrayOf(Direction.RIGHT, Direction.UP)
                ONE_FINGER_SWIPE_RIGHT_THEN_DOWN -> arrayOf(Direction.RIGHT, Direction.DOWN)
                ONE_FINGER_SWIPE_RIGHT_THEN_LEFT -> arrayOf(Direction.RIGHT, Direction.LEFT)

                ONE_FINGER_SWIPE_DOWN_THEN_UP -> arrayOf(Direction.DOWN, Direction.UP)
                ONE_FINGER_SWIPE_DOWN_THEN_RIGHT -> arrayOf(Direction.DOWN, Direction.RIGHT)
                ONE_FINGER_SWIPE_DOWN_THEN_LEFT -> arrayOf(Direction.DOWN, Direction.LEFT)

                ONE_FINGER_SWIPE_LEFT_THEN_UP -> arrayOf(Direction.LEFT, Direction.UP)
                ONE_FINGER_SWIPE_LEFT_THEN_RIGHT -> arrayOf(Direction.LEFT, Direction.RIGHT)
                ONE_FINGER_SWIPE_LEFT_THEN_DOWN -> arrayOf(Direction.LEFT, Direction.DOWN)

                else -> arrayOf()
            }

            array.forEach { direction ->
                direction.fingers = 1
            }

            return array
        }

    private fun getString(context: Context, property: String): String {
        return context.getString("gesture_${identifier}_${property}")
    }

    override fun title(context: Context) = getString(context, "title")
    fun description(context: Context) = getString(context, "description")
    fun explanation(context: Context) = getString(context, "explanation")

    fun image(context: Context): Int {
        return context.resources.getIdentifier("gesture_${identifier}", "drawable", context.packageName)
    }

    fun view(context: Context): GestureView {
        return when (this) {
            ONE_FINGER_TOUCH -> TouchGestureView(context, this)
            ONE_FINGER_DOUBLE_TAP -> TapGestureView(context, this, 1, 2)
            ONE_FINGER_DOUBLE_TAP_HOLD -> TapGestureView(context, this, 1, 2, true)

            ONE_FINGER_SWIPE_RIGHT -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_LEFT -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_DOWN -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_UP -> SwipeGestureView(context, this)

            TWO_FINGER_SWIPE_DOWN -> ScrollGestureView(context, this)
            TWO_FINGER_SWIPE_UP -> ScrollGestureView(context, this)
            TWO_FINGER_SWIPE_RIGHT -> ScrollGestureView(context, this)
            TWO_FINGER_SWIPE_LEFT -> ScrollGestureView(context, this)

            ONE_FINGER_SWIPE_DOWN_THEN_UP -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_UP_THEN_DOWN -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_RIGHT_THEN_LEFT -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_LEFT_THEN_RIGHT -> SwipeGestureView(context, this)

            ONE_FINGER_SWIPE_DOWN_THEN_LEFT -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_UP_THEN_LEFT -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_LEFT_THEN_UP -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_RIGHT_THEN_DOWN -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_LEFT_THEN_DOWN -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_UP_THEN_RIGHT -> SwipeGestureView(context, this)
            ONE_FINGER_SWIPE_DOWN_THEN_RIGHT -> SwipeGestureView(context, this)
        }
    }

    override fun completed(context: Context): Boolean {
        return Preferences.isCompleted(this)
    }
    override fun completed(context: Context, completed: Boolean) {
        Preferences.setCompleted(this, true)
    }

    companion object {
        fun all(): ArrayList<Gesture> {
            return values().toCollection(arrayListOf())
        }

        fun from(gestureId: Int): Gesture? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                when (gestureId) {
                    // ONE FINGER TAP
                    AccessibilityService.GESTURE_DOUBLE_TAP -> return ONE_FINGER_DOUBLE_TAP
                    AccessibilityService.GESTURE_DOUBLE_TAP_AND_HOLD -> return null

                    // TWO FINGER TAP
                    AccessibilityService.GESTURE_2_FINGER_SINGLE_TAP -> return null
                    AccessibilityService.GESTURE_2_FINGER_DOUBLE_TAP -> return null
                    AccessibilityService.GESTURE_2_FINGER_DOUBLE_TAP_AND_HOLD -> return null
                    AccessibilityService.GESTURE_2_FINGER_TRIPLE_TAP -> return null

                    // TWO FINGER SWIPE
                    AccessibilityService.GESTURE_2_FINGER_SWIPE_UP -> return TWO_FINGER_SWIPE_UP
                    AccessibilityService.GESTURE_2_FINGER_SWIPE_RIGHT -> return TWO_FINGER_SWIPE_RIGHT
                    AccessibilityService.GESTURE_2_FINGER_SWIPE_DOWN -> return TWO_FINGER_SWIPE_DOWN
                    AccessibilityService.GESTURE_2_FINGER_SWIPE_LEFT -> return TWO_FINGER_SWIPE_LEFT

                    // THREE FINGER TAP
                    AccessibilityService.GESTURE_3_FINGER_SINGLE_TAP -> return null
                    AccessibilityService.GESTURE_3_FINGER_DOUBLE_TAP -> return null
                    AccessibilityService.GESTURE_3_FINGER_DOUBLE_TAP_AND_HOLD -> return null
                    AccessibilityService.GESTURE_3_FINGER_TRIPLE_TAP -> return null

                    // THREE FINGER SWIPE
                    AccessibilityService.GESTURE_3_FINGER_SWIPE_UP -> return null
                    AccessibilityService.GESTURE_3_FINGER_SWIPE_RIGHT -> return null
                    AccessibilityService.GESTURE_3_FINGER_SWIPE_DOWN -> return null
                    AccessibilityService.GESTURE_3_FINGER_SWIPE_LEFT -> return null

                    // FOUR FINGER TAP
                    AccessibilityService.GESTURE_4_FINGER_SINGLE_TAP -> return null
                    AccessibilityService.GESTURE_4_FINGER_DOUBLE_TAP -> return null
                    AccessibilityService.GESTURE_4_FINGER_DOUBLE_TAP_AND_HOLD -> return null
                    AccessibilityService.GESTURE_4_FINGER_TRIPLE_TAP -> return null

                    // FOUR FINGER SWIPE
                    AccessibilityService.GESTURE_4_FINGER_SWIPE_UP -> return null
                    AccessibilityService.GESTURE_4_FINGER_SWIPE_RIGHT -> return null
                    AccessibilityService.GESTURE_4_FINGER_SWIPE_DOWN -> return null
                    AccessibilityService.GESTURE_4_FINGER_SWIPE_LEFT -> return null
                }
            }

            return when (gestureId) {
                // ONE FINGER SWIPE
                AccessibilityService.GESTURE_SWIPE_UP -> return ONE_FINGER_SWIPE_UP
                AccessibilityService.GESTURE_SWIPE_RIGHT -> return ONE_FINGER_SWIPE_RIGHT
                AccessibilityService.GESTURE_SWIPE_DOWN -> return ONE_FINGER_SWIPE_DOWN
                AccessibilityService.GESTURE_SWIPE_LEFT -> return ONE_FINGER_SWIPE_LEFT

                // ONE FINGER SWIPE UP
                AccessibilityService.GESTURE_SWIPE_UP_AND_RIGHT -> return ONE_FINGER_SWIPE_UP_THEN_RIGHT
                AccessibilityService.GESTURE_SWIPE_UP_AND_DOWN -> return ONE_FINGER_SWIPE_UP_THEN_DOWN
                AccessibilityService.GESTURE_SWIPE_UP_AND_LEFT -> return ONE_FINGER_SWIPE_UP_THEN_LEFT

                // ONE FINGER SWIPE RIGHT
                AccessibilityService.GESTURE_SWIPE_RIGHT_AND_UP -> return null
                AccessibilityService.GESTURE_SWIPE_RIGHT_AND_DOWN -> return ONE_FINGER_SWIPE_RIGHT_THEN_DOWN
                AccessibilityService.GESTURE_SWIPE_RIGHT_AND_LEFT -> return ONE_FINGER_SWIPE_RIGHT_THEN_LEFT

                // ONE FINGER SWIPE DOWN
                AccessibilityService.GESTURE_SWIPE_DOWN_AND_UP -> return ONE_FINGER_SWIPE_DOWN_THEN_UP
                AccessibilityService.GESTURE_SWIPE_DOWN_AND_RIGHT -> return ONE_FINGER_SWIPE_DOWN_THEN_RIGHT
                AccessibilityService.GESTURE_SWIPE_DOWN_AND_LEFT -> return ONE_FINGER_SWIPE_DOWN_THEN_LEFT

                // ONE FINGER SWIPE LEFT
                AccessibilityService.GESTURE_SWIPE_LEFT_AND_UP -> return ONE_FINGER_SWIPE_LEFT_THEN_UP
                AccessibilityService.GESTURE_SWIPE_LEFT_AND_RIGHT -> return ONE_FINGER_SWIPE_LEFT_THEN_RIGHT
                AccessibilityService.GESTURE_SWIPE_LEFT_AND_DOWN -> return ONE_FINGER_SWIPE_LEFT_THEN_DOWN

                else -> null
            }
        }
    }
}