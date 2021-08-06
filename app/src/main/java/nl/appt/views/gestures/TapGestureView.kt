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
import nl.appt.model.Gesture
import nl.appt.model.Touch

/**
 * Created by Jan Jaap de Groot on 22/10/2020
 * Copyright 2020 Stichting Appt
 */

class TapGestureView(
    context: Context,
    gesture: Gesture,
    private val fingers: Int,
    private val taps: Int,
    private val longPress: Boolean = false
): GestureView(gesture, context) {

    private var tapped = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        if (event?.isStart() == true) {
            tapped = false
        } else if (event?.isEnd() == true) {
            if (!tapped) {
                incorrect("Je maakte geen tik.")
            }
        }

        return true
    }

    override fun onAccessibilityGesture(gesture: AccessibilityGesture) {
        if (!tapped) {
            incorrect("Je maakte een veegbeweging in plaats van een tik.")
        }
    }

    private fun onTapped(fingers: Int, taps: Int, longPress: Boolean = false) {
        tapped = true

        // Add one tap if TalkBack is activated
        var actualTaps = taps
        if (Accessibility.isTalkBackEnabled(context)) {
            actualTaps += 1
        }

        if (fingers != this.fingers) {
            incorrect("Je tikte met $fingers vingers, gebruik ${this.fingers} vingers.")
            return
        }

        if (actualTaps != this.taps) {
            incorrect("Je tikte $actualTaps keer, tik ${this.taps} keer.")
            return
        }

        if (longPress != this.longPress) {
            if (longPress) {
                incorrect("Je hield het scherm te lang ingedrukt.")
            } else {
                incorrect("Je hield het scherm te kort ingedrukt.")
            }
            return
        }

        correct()
    }

    /**
     * Custom implementation of GestureDetector to detect taps
     */
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            showTouches(e, 2)
            onTapped(1, 2)
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            tapped = true // Set to true in advance to avoid triggering incorrect feedback.
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            onTapped(1, 1)
            return super.onSingleTapConfirmed(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            showTouches(e, 1, true)
            onTapped(1, 1, true)
        }
    })
}