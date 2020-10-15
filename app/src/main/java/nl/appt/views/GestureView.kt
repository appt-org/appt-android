package nl.appt.views

import android.accessibilityservice.AccessibilityService.*
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Direction
import nl.appt.model.Gesture

interface GestureViewCallback {
    fun correct(gesture: Gesture)
    fun incorrect(gesture: Gesture, feedback: String? = null)
}

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
open class GestureView(val gesture: Gesture, context: Context) : View(context) {

    private val TAG = "GestureView"
    private val CLASS_NAME = GestureView::class.java.name

    var callback: GestureViewCallback? = null

    override fun onHoverEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onHoverEvent: $event")

        if (Accessibility.isTalkBackEnabled(context)) {
            onTouchEvent(event)
        }

        return super.onHoverEvent(event)
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent?) {
        event?.className = CLASS_NAME
        super.onInitializeAccessibilityEvent(event)
    }

    override fun onPopulateAccessibilityEvent(event: AccessibilityEvent?) {
        event?.className = CLASS_NAME
        super.onPopulateAccessibilityEvent(event)
    }

    fun correct() {
        callback?.correct(gesture)
    }

    fun incorrect(feedback: String? = "Geen feedback") {
        callback?.incorrect(gesture, feedback)
    }

    open fun onAccessibilityGesture(gesture: AccessibilityGesture) {
        // Can be overridden
    }


}