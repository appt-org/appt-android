package nl.appt.model

import android.content.Context
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

    ONE_FINGER_SWIPE_RIGHT,
    ONE_FINGER_SWIPE_LEFT,
    ONE_FINGER_SWIPE_UP,
    ONE_FINGER_SWIPE_DOWN,

    TWO_FINGER_SWIPE_DOWN,
    TWO_FINGER_SWIPE_UP,
    TWO_FINGER_SWIPE_RIGHT,
    TWO_FINGER_SWIPE_LEFT,

    ONE_FINGER_SWIPE_UP_THEN_DOWN,
    ONE_FINGER_SWIPE_DOWN_THEN_UP,
    ONE_FINGER_SWIPE_RIGHT_THEN_LEFT,
    ONE_FINGER_SWIPE_LEFT_THEN_RIGHT,

    ONE_FINGER_SWIPE_DOWN_THEN_LEFT,
    ONE_FINGER_SWIPE_UP_THEN_LEFT,
    ONE_FINGER_SWIPE_LEFT_THEN_UP,
    ONE_FINGER_SWIPE_RIGHT_THEN_DOWN,
    ONE_FINGER_SWIPE_LEFT_THEN_DOWN,
    ONE_FINGER_SWIPE_UP_THEN_RIGHT,
    ONE_FINGER_SWIPE_DOWN_THEN_RIGHT;

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

            ONE_FINGER_SWIPE_RIGHT -> SwipeGestureView(context, this, Direction.RIGHT)
            ONE_FINGER_SWIPE_LEFT -> SwipeGestureView(context, this, Direction.LEFT)
            ONE_FINGER_SWIPE_DOWN -> SwipeGestureView(context, this, Direction.DOWN)
            ONE_FINGER_SWIPE_UP -> SwipeGestureView(context, this, Direction.UP)

            TWO_FINGER_SWIPE_DOWN -> ScrollGestureView(context, this, Direction.DOWN)
            TWO_FINGER_SWIPE_UP -> ScrollGestureView(context, this, Direction.UP)
            TWO_FINGER_SWIPE_RIGHT -> ScrollGestureView(context, this, Direction.RIGHT)
            TWO_FINGER_SWIPE_LEFT -> ScrollGestureView(context, this, Direction.LEFT)

            ONE_FINGER_SWIPE_DOWN_THEN_UP -> SwipeGestureView(context, this, Direction.DOWN, Direction.UP)
            ONE_FINGER_SWIPE_UP_THEN_DOWN -> SwipeGestureView(context, this, Direction.UP, Direction.DOWN)
            ONE_FINGER_SWIPE_RIGHT_THEN_LEFT -> SwipeGestureView(context, this, Direction.RIGHT, Direction.LEFT)
            ONE_FINGER_SWIPE_LEFT_THEN_RIGHT -> SwipeGestureView(context, this, Direction.LEFT, Direction.RIGHT)

            ONE_FINGER_SWIPE_DOWN_THEN_LEFT -> SwipeGestureView(context, this, Direction.DOWN, Direction.LEFT)
            ONE_FINGER_SWIPE_UP_THEN_LEFT -> SwipeGestureView(context, this, Direction.UP, Direction.LEFT)
            ONE_FINGER_SWIPE_LEFT_THEN_UP -> SwipeGestureView(context, this, Direction.LEFT, Direction.UP)
            ONE_FINGER_SWIPE_RIGHT_THEN_DOWN -> SwipeGestureView(context, this, Direction.RIGHT, Direction.DOWN)
            ONE_FINGER_SWIPE_LEFT_THEN_DOWN -> SwipeGestureView(context, this, Direction.LEFT, Direction.DOWN)
            ONE_FINGER_SWIPE_UP_THEN_RIGHT -> SwipeGestureView(context, this, Direction.UP, Direction.RIGHT)
            ONE_FINGER_SWIPE_DOWN_THEN_RIGHT -> SwipeGestureView(context, this, Direction.DOWN, Direction.RIGHT)
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
    }
}