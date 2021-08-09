package nl.appt.views.gestures

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.extensions.isEnd
import nl.appt.model.Gesture

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

    private var tapCount = 0
    private var longPressed = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        if (!gestureDetector.onTouchEvent(event)) {
            if (event?.isEnd() == true && tapCount == 0) {
                incorrect("Je veegde op het scherm. Tik op het scherm.")
            }
        }

        return true
    }

    override fun onAccessibilityGesture(gesture: Gesture) {
        if (this.gesture == gesture) {
            correct()
        } else {
            incorrect("Je veegde op het scherm. Tik op het scherm.")
        }
    }

    private fun onTapped(fingers: Int, taps: Int, longPress: Boolean = false) {
        // Check amount of fingers
        if (fingers != this.fingers) {
            incorrect("Tik met ${this.fingers} vingers. Je tikte met $fingers vingers.")
            return
        }

        // Add one tap if TalkBack is activated
        var actualTaps = taps
        if (Accessibility.isTalkBackEnabled(context)) {
            actualTaps += 1
        }
        // Check amount of taps
        if (actualTaps != this.taps) {
            incorrect("Tik ${this.taps} keer. Je tikte $actualTaps keer.")
            return
        }

        // Check if long pressed
        if (longPress != this.longPress) {
            val timeout = ViewConfiguration.getLongPressTimeout()

            Handler(Looper.getMainLooper()).postDelayed({
                if (longPress) {
                    incorrect("Houd het scherm korter ingedrukt na het tikken.")
                } else {
                    incorrect("Houd het scherm langer ingedrukt na het tikken.")
                }
            }, timeout.toLong())

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

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            tapCount = 1
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            tapCount = 1

            showTouches(e, tapCount)
            onTapped (e?.pointerCount ?: 1, tapCount)

            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            tapCount = 2

            showTouches(e, tapCount)
            onTapped(e?.pointerCount ?: 1, tapCount)

            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            longPressed = true

            if (tapCount == 0) {
                tapCount = 1
            }

            showTouches(e, tapCount, longPressed)
            onTapped(e?.pointerCount ?: 1, tapCount, longPressed)
        }
    })
}