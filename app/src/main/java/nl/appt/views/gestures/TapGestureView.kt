package nl.appt.views.gestures

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import it.sephiroth.android.library.uigestures.UIGestureRecognizer
import it.sephiroth.android.library.uigestures.UIGestureRecognizerDelegate
import it.sephiroth.android.library.uigestures.UITapGestureRecognizer
import it.sephiroth.android.library.uigestures.setGestureDelegate
import nl.appt.model.Gesture
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

    private val gestureListener = { recognizer: UIGestureRecognizer ->
        Log.d(TAG, "Gesture recognized")

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
                this@TapGestureView.onTouchEvent(event)
                return super.onTouchEvent(event)
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

        val twoFingerTripleTapRecognizer = tapGestureRecognizer(2, 3)
        val twoFingerTwoTapRecognizer = tapGestureRecognizer(2, 2, twoFingerTripleTapRecognizer)
        val twoFingerOneTapRecognizer = tapGestureRecognizer(2, 1, twoFingerTwoTapRecognizer)

        val oneFingerTripleTapRecognizer = tapGestureRecognizer(1, 3, twoFingerTripleTapRecognizer)
        val oneFingerTwoTapRecognizer = tapGestureRecognizer(1, 2, oneFingerTripleTapRecognizer)
        val oneFingerOneTapRecognizer = tapGestureRecognizer(1, 1, oneFingerTwoTapRecognizer)

        delegate.addGestureRecognizer(twoFingerTripleTapRecognizer)
        delegate.addGestureRecognizer(twoFingerTwoTapRecognizer)
        delegate.addGestureRecognizer(twoFingerOneTapRecognizer)

        delegate.addGestureRecognizer(oneFingerTripleTapRecognizer)
        delegate.addGestureRecognizer(oneFingerTwoTapRecognizer)
        delegate.addGestureRecognizer(oneFingerOneTapRecognizer)

        setGestureDelegate(delegate)
    }

    override fun onAccessibilityGesture(gesture: Gesture) {
        if (this.gesture == gesture) {
            correct()
        } else {
            incorrect("Je veegde op het scherm. Tik op het scherm.")
        }
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