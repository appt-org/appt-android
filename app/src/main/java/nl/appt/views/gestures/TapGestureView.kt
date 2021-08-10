package nl.appt.views.gestures

import android.content.Context
import android.view.MotionEvent
import it.sephiroth.android.library.uigestures.*
import nl.appt.extensions.isEnd
import nl.appt.extensions.isStart
import nl.appt.model.Gesture
import nl.appt.model.Touch
import nl.appt.services.ApptService

/**
 * Created by Jan Jaap de Groot on 22/10/2020
 * Copyright 2020 Stichting Appt
 */
class TapGestureView(
    context: Context,
    gesture: Gesture
): GestureView(gesture, context) {

    private val TAG = "TapGestureView"
    private var tapped = false

    private val gestureListener = { recognizer: UIGestureRecognizer ->
        var fingers = 1
        var taps = 1
        var hold = false

        if (recognizer is UITapGestureRecognizer) {
            fingers = recognizer.touchesRequired
            taps = recognizer.tapsRequired
        } else if (recognizer is UILongPressGestureRecognizer) {
            fingers = recognizer.touchesRequired
            taps = recognizer.tapsRequired
            hold = true
        }

        if (ApptService.isEnabled(context)) {
            taps += 1
        }

        when {
            fingers != gesture.fingers -> {
                incorrect("Tik met ${gesture.fingers} vingers. Je tikte met $fingers vingers.")
            }
            taps != gesture.taps -> {
                incorrect("Tik ${gesture.taps} keer. Je tikte $taps keer.")
            }
            hold != gesture.hold -> {
                if (hold) {
                    incorrect("Houd het scherm korter ingedrukt na het tikken.")
                } else {
                    incorrect("Houd het scherm langer ingedrukt na het tikken.")
                }
            }
            else -> {
                correct()
            }
        }

        showTap(taps, hold)
    }

    private fun tapGestureRecognizer(fingers: Int, taps: Int, requiresFailureOf: UIGestureRecognizer? = null): UITapGestureRecognizer {
        val recognizer = object : UITapGestureRecognizer(context) {
            override fun onTouchEvent(event: MotionEvent): Boolean {
                val result = super.onTouchEvent(event)
                if (requiresFailureOf == null) {
                    this@TapGestureView.onTouchEvent(event)
                }
                return result
            }
        }
        recognizer.tag = "tap-$fingers-fingers-$taps-taps"
        recognizer.touchesRequired = fingers
        recognizer.tapsRequired = taps
        recognizer.requireFailureOf = requiresFailureOf
        recognizer.actionListener = gestureListener
        return recognizer
    }

    private fun longPressRecognizer(fingers: Int, taps: Int, requiresFailureOf: UIGestureRecognizer? = null): UILongPressGestureRecognizer {
        val recognizer = object : UILongPressGestureRecognizer(context) {
            override fun onTouchEvent(event: MotionEvent): Boolean {
                val result = super.onTouchEvent(event)
                if (requiresFailureOf == null) {
                    this@TapGestureView.onTouchEvent(event)
                }
                return result
            }
        }
        recognizer.tag = "long-press-$fingers-fingers-$taps-taps"
        recognizer.touchesRequired = fingers
        recognizer.tapsRequired = taps
        recognizer.requireFailureOf = requiresFailureOf
        recognizer.actionListener = gestureListener
        return recognizer
    }

    init {
        val delegate = UIGestureRecognizerDelegate()

        val fourFingerLongPressRecognizer = longPressRecognizer(4, 2)
        val threeFingerLongPressRecognizer = longPressRecognizer(3, 2, fourFingerLongPressRecognizer)
        val twoFingerLongPressRecognizer = longPressRecognizer(2, 2, threeFingerLongPressRecognizer)
        val oneFingerLongPressRecognizer = longPressRecognizer(1, 2, twoFingerLongPressRecognizer)

        val fourFingerTripleTapRecognizer = tapGestureRecognizer(4, 3, threeFingerLongPressRecognizer)
        val fourFingerTwoTapRecognizer = tapGestureRecognizer(4, 2, fourFingerTripleTapRecognizer)
        val fourFingerOneTapRecognizer = tapGestureRecognizer(4, 1, fourFingerTwoTapRecognizer)

        val threeFingerTripleTapRecognizer = tapGestureRecognizer(3, 3, fourFingerTripleTapRecognizer)
        val threeFingerTwoTapRecognizer = tapGestureRecognizer(3, 2, threeFingerTripleTapRecognizer)
        val threeFingerOneTapRecognizer = tapGestureRecognizer(3, 1, threeFingerTwoTapRecognizer)

        val twoFingerTripleTapRecognizer = tapGestureRecognizer(2, 3, threeFingerTripleTapRecognizer)
        val twoFingerTwoTapRecognizer = tapGestureRecognizer(2, 2, twoFingerTripleTapRecognizer)
        val twoFingerOneTapRecognizer = tapGestureRecognizer(2, 1, twoFingerTwoTapRecognizer)

        val oneFingerTripleTapRecognizer = tapGestureRecognizer(1, 3, twoFingerTripleTapRecognizer)
        val oneFingerTwoTapRecognizer = tapGestureRecognizer(1, 2, oneFingerTripleTapRecognizer)
        val oneFingerOneTapRecognizer = tapGestureRecognizer(1, 1, oneFingerTwoTapRecognizer)

        delegate.addGestureRecognizer(fourFingerLongPressRecognizer)
        delegate.addGestureRecognizer(threeFingerLongPressRecognizer)
        delegate.addGestureRecognizer(twoFingerLongPressRecognizer)
        delegate.addGestureRecognizer(oneFingerLongPressRecognizer)

        delegate.addGestureRecognizer(fourFingerTripleTapRecognizer)
        delegate.addGestureRecognizer(fourFingerTwoTapRecognizer)
        delegate.addGestureRecognizer(fourFingerOneTapRecognizer)

        delegate.addGestureRecognizer(threeFingerTripleTapRecognizer)
        delegate.addGestureRecognizer(threeFingerTwoTapRecognizer)
        delegate.addGestureRecognizer(threeFingerOneTapRecognizer)

        delegate.addGestureRecognizer(twoFingerTripleTapRecognizer)
        delegate.addGestureRecognizer(twoFingerTwoTapRecognizer)
        delegate.addGestureRecognizer(twoFingerOneTapRecognizer)

        delegate.addGestureRecognizer(oneFingerTripleTapRecognizer)
        delegate.addGestureRecognizer(oneFingerTwoTapRecognizer)
        delegate.addGestureRecognizer(oneFingerOneTapRecognizer)

        setGestureDelegate(delegate)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.isStart() == true) {
            tapped = false
        } else if (event?.isEnd() == true && !tapped) {
            postDelayed({
                if (!tapped) {
                    incorrect("Je veegde op het scherm. Tik op het scherm.")
                }
            }, 500)
        }
        return super.onTouchEvent(event)
    }

    override fun onAccessibilityGesture(gesture: Gesture) {
        if (this.gesture == gesture) {
            correct()
        } else {
            incorrect("Je veegde op het scherm. Tik op het scherm.")
        }
    }

    private fun showTap(taps: Int, longPress: Boolean) {
        if (tapped) {
            return
        }

        tapped = true

        for (key in touches.keys) {
            touches[key]?.lastOrNull()?.let { lastTouch ->
                val touch = Touch(lastTouch.x, lastTouch.y, taps, longPress)
                touches[key] = arrayListOf(touch)
            }
        }
        invalidate()
    }
}