package nl.appt.model

import android.content.Context
import nl.appt.R
import nl.appt.extensions.getString
import nl.appt.extensions.identifier
import nl.appt.helpers.Preferences
import nl.appt.views.gestures.GestureView
import nl.appt.views.gestures.SwipeGestureView
import nl.appt.views.gestures.TapGestureView
import nl.appt.views.gestures.TouchGestureView
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
enum class Gesture: Training, Serializable {

    TOUCH,
    DOUBLE_TAP,

    SWIPE_RIGHT,
    SWIPE_LEFT,
    SWIPE_UP,
    SWIPE_DOWN,

    SWIPE_UP_DOWN,
    SWIPE_DOWN_UP,
    SWIPE_RIGHT_LEFT,
    SWIPE_LEFT_RIGHT,

    SWIPE_DOWN_LEFT,
    SWIPE_UP_LEFT,
    SWIPE_LEFT_UP,
    SWIPE_RIGHT_DOWN,
    SWIPE_LEFT_DOWN,
    SWIPE_UP_RIGHT,
    SWIPE_DOWN_RIGHT;

    private fun getString(context: Context, property: String): String {
        return context.getString("talkback_gesture_${identifier}_${property}")
    }

    override fun title(context: Context) = getString(context, "title")
    fun description(context: Context) = getString(context, "description")
    fun explanation(context: Context) = getString(context, "explanation")

    fun view(context: Context): GestureView {
        return when (this) {
            TOUCH -> TouchGestureView(context, this)
            DOUBLE_TAP -> TapGestureView(context, this, 1, 2)

            SWIPE_RIGHT -> SwipeGestureView(context, this, Direction.RIGHT)
            SWIPE_LEFT -> SwipeGestureView(context, this, Direction.LEFT)
            SWIPE_UP -> SwipeGestureView(context, this, Direction.UP)
            SWIPE_DOWN -> SwipeGestureView(context, this, Direction.DOWN)

            SWIPE_DOWN_UP -> SwipeGestureView(context, this, Direction.DOWN, Direction.UP)
            SWIPE_UP_DOWN -> SwipeGestureView(context, this, Direction.UP, Direction.DOWN)
            SWIPE_RIGHT_LEFT -> SwipeGestureView(context, this, Direction.RIGHT, Direction.LEFT)
            SWIPE_LEFT_RIGHT -> SwipeGestureView(context, this, Direction.LEFT, Direction.RIGHT)

            SWIPE_DOWN_LEFT -> SwipeGestureView(context, this, Direction.DOWN, Direction.LEFT)
            SWIPE_UP_LEFT -> SwipeGestureView(context, this, Direction.UP, Direction.LEFT)
            SWIPE_LEFT_UP -> SwipeGestureView(context, this, Direction.LEFT, Direction.UP)
            SWIPE_RIGHT_DOWN -> SwipeGestureView(context, this, Direction.RIGHT, Direction.DOWN)
            SWIPE_LEFT_DOWN -> SwipeGestureView(context, this, Direction.LEFT, Direction.DOWN)
            SWIPE_UP_RIGHT -> SwipeGestureView(context, this, Direction.UP, Direction.RIGHT)
            SWIPE_DOWN_RIGHT -> SwipeGestureView(context, this, Direction.DOWN, Direction.RIGHT)
        }
    }

    override fun completed(context: Context): Boolean {
        return Preferences(context).isCompleted(this)
    }
    override fun completed(context: Context, completed: Boolean) {
        Preferences(context).setCompleted(this, true)
    }

    companion object {
        fun all(): ArrayList<Gesture> {
            return values().toCollection(arrayListOf())
        }
    }
}