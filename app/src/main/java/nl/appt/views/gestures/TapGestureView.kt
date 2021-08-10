package nl.appt.views.gestures

import android.content.Context
import android.view.MotionEvent
import it.sephiroth.android.library.uigestures.UIGestureRecognizer
import it.sephiroth.android.library.uigestures.UIGestureRecognizerDelegate
import it.sephiroth.android.library.uigestures.UITapGestureRecognizer
import it.sephiroth.android.library.uigestures.setGestureDelegate
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
        tapped = true

        if (recognizer is UITapGestureRecognizer) {
            val fingers = recognizer.touchesRequired

            var taps = recognizer.tapsRequired
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
                else -> {
                    correct()
                }
            }
        }
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
        recognizer.tag = "fingers-$fingers-taps-$taps"
        recognizer.touchesRequired = fingers
        recognizer.tapsRequired = taps
        recognizer.requireFailureOf = requiresFailureOf
        recognizer.actionListener = gestureListener
        return recognizer
    }

    init {
        val delegate = UIGestureRecognizerDelegate()

        val threeFingerTripleTapRecognizer = tapGestureRecognizer(3, 3)
        val threeFingerTwoTapRecognizer = tapGestureRecognizer(3, 2, threeFingerTripleTapRecognizer)
        val threeFingerOneTapRecognizer = tapGestureRecognizer(3, 1, threeFingerTwoTapRecognizer)

        val twoFingerTripleTapRecognizer = tapGestureRecognizer(2, 3, threeFingerTripleTapRecognizer)
        val twoFingerTwoTapRecognizer = tapGestureRecognizer(2, 2, twoFingerTripleTapRecognizer)
        val twoFingerOneTapRecognizer = tapGestureRecognizer(2, 1, twoFingerTwoTapRecognizer)

        val oneFingerTripleTapRecognizer = tapGestureRecognizer(1, 3, twoFingerTripleTapRecognizer)
        val oneFingerTwoTapRecognizer = tapGestureRecognizer(1, 2, oneFingerTripleTapRecognizer)
        val oneFingerOneTapRecognizer = tapGestureRecognizer(1, 1, oneFingerTwoTapRecognizer)

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
            incorrect("Je veegde op het scherm. Tik op het scherm.")
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

    override fun correct() {
        // Show tap
        for (key in touches.keys) {
            touches[key]?.lastOrNull()?.let { lastTouch ->
                val touch = Touch(lastTouch.x, lastTouch.y, gesture.taps, gesture.longPress)
                touches[key] = arrayListOf(touch)
            }
        }
        invalidate()

        super.correct()
    }

    override fun incorrect(feedback: String) {
        postDelayed({
            if (!tapped) {
                super.incorrect(feedback)
            }
        }, 500)
    }

//    private fun onTapped(fingers: Int, taps: Int, longPress: Boolean = false) {
//        Log.d(TAG, "onTapped, fingers: $fingers, taps: $taps, longPress: $longPress")
//
//        // Check amount of fingers
//        if (fingers != gesture.fingers) {
//            incorrect("Tik met ${gesture.fingers} vingers. Je tikte met $fingers vingers.")
//            return
//        }
//
//        // Add one tap if TalkBack is activated
//        var actualTaps = taps
//        if (Accessibility.isTalkBackEnabled(context)) {
//            actualTaps += 1
//        }
//        // Check amount of taps
//        if (actualTaps != gesture.taps) {
//            incorrect("Tik ${gesture.taps} keer. Je tikte $actualTaps keer.")
//            return
//        }
//
//        // Check if long pressed
//        if (longPress != gesture.longPress) {
//            val timeout = ViewConfiguration.getLongPressTimeout()
//
//            Handler(Looper.getMainLooper()).postDelayed({
//                if (longPress) {
//                    incorrect("Houd het scherm korter ingedrukt na het tikken.")
//                } else {
//                    incorrect("Houd het scherm langer ingedrukt na het tikken.")
//                }
//            }, timeout.toLong())
//
//            return
//        }
//
//        correct()
//    }
}