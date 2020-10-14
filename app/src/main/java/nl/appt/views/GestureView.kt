package nl.appt.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class GestureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = "GestureView"
    private val CLASS_NAME = GestureView::class.java.name

    override fun onHoverEvent(event: MotionEvent?): Boolean {
        //Log.d(TAG, "onHoverEvent: $event")

        if (Accessibility.isTalkBackEnabled(context)) {
            //onTouchEvent(event)
        }

        return super.onHoverEvent(event)
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent?) {
        //Log.d(TAG, "onInitializeAccessibilityEvent: $event")
        event?.className = CLASS_NAME
        super.onInitializeAccessibilityEvent(event)
    }

    override fun onPopulateAccessibilityEvent(event: AccessibilityEvent?) {
        //Log.d(TAG, "onPopulateAccessibilityEvent: $event")
        event?.className = CLASS_NAME
        super.onPopulateAccessibilityEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //Log.d(TAG, "onTouchEvent")
        return super.onTouchEvent(event)
    }
}