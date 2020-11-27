package nl.appt.tabs.training.gestures

import android.content.*
import android.view.View
import kotlinx.android.synthetic.main.activity_training.*
import nl.appt.R
import nl.appt.widgets.ToolbarActivity
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.activity.accessibility
import nl.appt.accessibility.announce
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.accessibility.setFocus
import nl.appt.accessibility.view.accessibility
import nl.appt.extensions.*
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Constants
import nl.appt.model.Gesture
import nl.appt.services.ApptService
import nl.appt.views.gestures.GestureView
import nl.appt.views.gestures.GestureViewCallback
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class GestureActivity: ToolbarActivity(), GestureViewCallback {

    private val TAG = "GestureActivity"

    private val gestures: ArrayList<Gesture>? by lazy {
        intent.getGestures()
    }

    private val gesture: Gesture by lazy {
        gestures?.firstOrNull() ?: intent.getGesture() ?: Gesture.TOUCH
    }

    private lateinit var gestureView: GestureView

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Kill check
            intent?.getBooleanExtra(Constants.SERVICE_KILLED, false)?.let { killed ->
                if (killed) {
                    finish()
                }
            }

            // Gesture check
            (intent?.getSerializableExtra(Constants.SERVICE_GESTURE) as? AccessibilityGesture)?.let { gesture ->
                gestureView.onAccessibilityGesture(gesture)
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_training

    override fun getToolbarTitle() = gesture.title

    override fun onViewCreated() {
        super.onViewCreated()

        // Show description
        descriptionTextView.text = gesture.description

        // Initialize GestureView
        gestureView = gesture.view(this)
        gestureView.callback = this
        gestureView.accessibility.label = gesture.description
        container.addView(gestureView)
        accessibility.elements = arrayOf(gestureView)

        // Listen to events from ApptService
        val filter = IntentFilter()
        filter.addAction(Constants.SERVICE_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        if (Accessibility.isTalkBackEnabled(this)) {
            if (ApptService.isEnabled(this)) {
                Timer().schedule(500) {
                    runOnUiThread {
                        Accessibility.setFocus(gestureView)
                    }
                }
            } else {
                toast(R.string.service_inactive) {
                    finish()
                }
            }
        }
    }

    override fun correct(gesture: Gesture) {
        gesture.completed(baseContext, true)
        setResult(RESULT_OK)
        feedbackTextView.visibility = View.GONE

        gestures?.let { gestures ->
            if (gestures.size > 1) {
                toast("Gebaar correct uitgevoerd!") {
                    gestures.removeAt(0)

                    startActivity<GestureActivity> {
                        setGestures(gestures)
                    }

                    finish()
                }
            } else {
                toast("Gefeliciteerd, je hebt alle gebaren correct uitgevoerd!") {
                    finish()
                }
            }
        } ?: run {
            toast("Gebaar correct uitgevoerd!") {
                finish()
            }
        }
    }

    override fun incorrect(gesture: Gesture, feedback: String) {
        Accessibility.announce(baseContext, feedback)
        feedbackTextView.text = feedback
        feedbackTextView.visibility = View.VISIBLE
    }
}