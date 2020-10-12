package nl.appt.services

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED
import android.view.accessibility.AccessibilityNodeInfo

/**
 * This is where the magic happens.
 * Running an AccessibilityService makes it "bypass" TalkBack.
 * The ApptService hooks into accessibility events and gestures.
 *
 * Created by Jan Jaap de Groot on 09/10/2020
 * Copyright 2020 Stichting Appt
 */
class ApptService: AccessibilityService() {

    private val TAG = "AccessibilityService"

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "Service connected")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onInterrupt() {
        Log.i(TAG, "onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.i(TAG, "onAcccessibilityEvent: $event")

        //Toast.makeText(this, "Event: $event", Toast.LENGTH_SHORT).show()

        event?.eventType?.let { type ->
            if (type == TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                Log.i(TAG, "Focused on: ${event.className}")

                event.source?.apply {
                    performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                    recycle()
                }
            }
        }
    }

    override fun onGesture(gestureId: Int): Boolean {
        Log.i(TAG, "onGesture: $gestureId")

        when (gestureId) {
            // UP
            GESTURE_SWIPE_UP -> {
                Log.i(TAG, "GESTURE_SWIPE_UP")
            }
            GESTURE_SWIPE_UP_AND_RIGHT -> {
                Log.i(TAG, "GESTURE_SWIPE_UP_AND_RIGHT")
            }
            GESTURE_SWIPE_UP_AND_DOWN -> {
                Log.i(TAG, "GESTURE_SWIPE_UP_AND_DOWN")
            }
            GESTURE_SWIPE_UP_AND_LEFT -> {
                Log.i(TAG, "GESTURE_SWIPE_UP_AND_LEFT")
            }

            // RIGHT
            GESTURE_SWIPE_RIGHT -> {
                Log.i(TAG, "GESTURE_SWIPE_RIGHT")
            }
            GESTURE_SWIPE_RIGHT_AND_UP -> {
                Log.i(TAG, "GESTURE_SWIPE_RIGHT_AND_UP")
            }
            GESTURE_SWIPE_RIGHT_AND_DOWN -> {
                Log.i(TAG, "GESTURE_SWIPE_RIGHT_AND_DOWN")
            }
            GESTURE_SWIPE_RIGHT_AND_LEFT -> {
                Log.i(TAG, "GESTURE_SWIPE_RIGHT_AND_LEFT")
            }

            // DOWN
            GESTURE_SWIPE_DOWN -> {
                Log.i(TAG, "GESTURE_SWIPE_DOWN")
            }
            GESTURE_SWIPE_DOWN_AND_UP -> {
                Log.i(TAG, "GESTURE_SWIPE_DOWN_AND_UP")
            }
            GESTURE_SWIPE_DOWN_AND_RIGHT -> {
                Log.i(TAG, "GESTURE_SWIPE_DOWN_AND_RIGHT")
            }
            GESTURE_SWIPE_DOWN_AND_LEFT -> {
                Log.i(TAG, "GESTURE_SWIPE_DOWN_AND_LEFT")
            }

            // LEFT
            GESTURE_SWIPE_LEFT -> {
                Log.i(TAG, "GESTURE_SWIPE_LEFT")
            }
            GESTURE_SWIPE_LEFT_AND_UP -> {
                Log.i(TAG, "GESTURE_SWIPE_LEFT_AND_UP")
            }
            GESTURE_SWIPE_LEFT_AND_RIGHT -> {
                Log.i(TAG, "GESTURE_SWIPE_LEFT_AND_RIGHT")
            }
            GESTURE_SWIPE_LEFT_AND_DOWN -> {
                Log.i(TAG, "GESTURE_SWIPE_LEFT_AND_DOWN")
            }
        }

        return super.onGesture(gestureId)
    }
}