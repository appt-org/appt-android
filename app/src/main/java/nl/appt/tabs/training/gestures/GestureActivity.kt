package nl.appt.tabs.training.gestures

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.*
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_training.*
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.widgets.ToolbarActivity
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.activity.accessibility
import nl.appt.accessibility.announce
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.accessibility.setFocus
import nl.appt.accessibility.view.accessibility
import nl.appt.extensions.getGesture
import nl.appt.extensions.getLaunch
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
    private val APPT_SERVICE = ApptService::class.java.name

    private val gesture: Gesture by lazy {
        intent.getGesture() ?: Gesture.SWIPE_DOWN_UP
    }

    private lateinit var gestureView: GestureView

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Kill check
            intent?.getBooleanExtra(Constants.SERVICE_KILLED, false)?.let { killed ->
                if (killed) {
                    toast("Appt service staat uit")
                    onBackPressed()
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

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG, "onNewIntent") // Called after relaunching from ApptService
        super.onNewIntent(intent)
    }

    override fun onBackPressed() {
        if (intent.getLaunch()) {
            startActivity<MainActivity>()
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        if (Accessibility.isTalkBackEnabled(this)) {
            if (isApptServiceEnabled()) {
                Timer().schedule(500) {
                    runOnUiThread {
                        Accessibility.setFocus(gestureView)
                    }
                }
            } else {
                enableApptService()
            }
        }
    }

    private fun isApptServiceEnabled(): Boolean {
        (getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).let { manager ->
            val services = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            for (service in services) {
                Log.d(TAG, "Service: ${service.resolveInfo.serviceInfo.name}")
                if (service.resolveInfo.serviceInfo.name == APPT_SERVICE) {
                    return true
                }
            }
        }
        return false
    }

    private fun enableApptService() {
        AlertDialog.Builder(this)
            .setTitle("Appt service aanzetten")
            .setMessage("De app kan alleen gebaren herkennen indien je de Appt service aanzet.")
            .setPositiveButton(android.R.string.yes) { _, _ ->
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                startActivity(intent)
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                onBackPressed()
            }
            .show()
    }

    override fun correct(gesture: Gesture) {
        gesture.completed(baseContext, true)
        setResult(RESULT_OK)
        feedbackTextView.visibility = View.GONE

        toast("Gebaar correct uitgevoerd!") {
            onBackPressed()
        }
    }

    override fun incorrect(gesture: Gesture, feedback: String) {
        Accessibility.announce(baseContext, feedback)
        feedbackTextView.text = feedback
        feedbackTextView.visibility = View.VISIBLE
    }
}