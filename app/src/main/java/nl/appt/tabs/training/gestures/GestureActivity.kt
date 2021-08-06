package nl.appt.tabs.training.gestures

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_training.*
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.activity.accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.accessibility.setFocus
import nl.appt.accessibility.view.accessibility
import nl.appt.extensions.*
import nl.appt.helpers.Events
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Constants
import nl.appt.model.Gesture
import nl.appt.services.ApptService
import nl.appt.views.gestures.GestureView
import nl.appt.views.gestures.GestureViewCallback
import nl.appt.widgets.ToolbarActivity
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
        gestures?.firstOrNull() ?: intent.getGesture() ?: Gesture.ONE_FINGER_TOUCH
    }
    private lateinit var gestureView: GestureView

    private var dialog: AlertDialog? = null
    private var errorLimit = 5
    private var errorCount = 0
    private var finished = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Received kill event
            intent?.getBooleanExtra(Constants.SERVICE_KILLED, false)?.let { killed ->
                Log.d(TAG, "ApptService has been killed")
            }

            // Received gesture event
            (intent?.getSerializableExtra(Constants.SERVICE_GESTURE) as? AccessibilityGesture)?.let { gesture ->
                if (dialog != null) {
                    dialog?.onAccessibilityGesture(gesture) // Pass gesture to AlertDialog if shown.
                } else {
                    gestureView.onAccessibilityGesture(gesture) // Pass gesture to GestureView.
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_training

    override fun getToolbarTitle() = ""

    override fun onViewCreated() {
        super.onViewCreated()

        titleTextView.text = gesture.title(this)
        descriptionTextView.text = gesture.description(this)
        gestureImageView.setImageResource(gesture.image(this))

        // Initialize GestureView
        gestureView = gesture.view(this)
        gestureView.callback = this
        gestureView.accessibility.label = String.format("%s: %s", titleTextView.text, descriptionTextView.text)
        container.addView(gestureView)
        accessibility.elements = arrayOf(gestureView)

        // Listen to events from ApptService
        val filter = IntentFilter()
        filter.addAction(Constants.SERVICE_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.explanation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_explanation) {
            showDialog(title = gesture.title(this), message = gesture.explanation(this))
            return true
        }
        return false
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        dialog?.dismiss()
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
                showError(R.string.service_inactive) {
                    finish()
                }
            }
        }
    }

    override fun correct(gesture: Gesture) {
        finished = true
        feedbackTextView.visibility = View.GONE

        events.log(Events.Category.gestureCompleted, gesture.identifier, errorCount)
        gesture.completed(baseContext, true)
        setResult(RESULT_OK)

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
        if (finished) {
            return
        }

        feedbackTextView.animate()
            .alpha(0.0f)
            .setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animator: Animator?) {
                    if (finished) {
                        return
                    }
                    feedbackTextView.visibility = View.VISIBLE
                    feedbackTextView.text = feedback
                    feedbackTextView.animate().alpha(1.0f).setDuration(250)
                }
            })

        errorCount++

        if (errorCount >= errorLimit) {
            var message = "Je hebt het gebaar $errorCount keer fout uitgevoerd. Wil je doorgaan of stoppen?"

            if (Accessibility.isTalkBackEnabled(this)) {
                message += "\n\nVeeg naar links om te stoppen.\n\nVeeg naar rechts om door te gaan."
            }

            dialog = AlertDialog.Builder(this)
                        .setMessage(message)
                        .setPositiveButton(R.string.action_continue) { _, _ ->
                            errorLimit *= 2
                        }
                        .setNegativeButton(R.string.action_stop) { _, _ ->
                            finish()
                        }
                        .setCancelable(false)
                        .show()
        }
    }
}