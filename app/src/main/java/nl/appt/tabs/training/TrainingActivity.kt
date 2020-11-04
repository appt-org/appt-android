package nl.appt.tabs.training

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
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Constants
import nl.appt.model.Gesture
import nl.appt.services.ApptService
import nl.appt.views.GestureView
import nl.appt.views.GestureViewCallback
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingActivity: ToolbarActivity() {

    private val TAG = "TrainingActivity"
    private val APPT_SERVICE = ApptService::class.java.name

    private val gesture: Gesture by lazy {
        var gesture = Gesture.SWIPE_DOWN_UP
        (intent.getSerializableExtra("gesture") as? Gesture)?.let {
            gesture = it
        }
        gesture
    }

    private lateinit var gestureView: GestureView

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Kill check
            intent?.getBooleanExtra(Constants.SERVICE_KILLED, false)?.let { killed ->
                if (killed) {
                    Toast.makeText(baseContext, "Appt service staat uit", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }

            // Gesture check
            (intent?.getSerializableExtra(Constants.SERVICE_GESTURE) as? AccessibilityGesture)?.let { gesture ->
                gestureView.onAccessibilityGesture(gesture)
            }
        }
    }

    private val callback = object : GestureViewCallback {
        override fun correct(gesture: Gesture) {
            Toast.makeText(baseContext, "Gebaar correct uitgevoerd!", Toast.LENGTH_SHORT).show()
            feedbackTextView.visibility = View.GONE
            onBackPressed()
        }

        override fun incorrect(gesture: Gesture, feedback: String) {
            Accessibility.announce(baseContext, feedback)
            feedbackTextView.text = feedback
            feedbackTextView.visibility = View.VISIBLE
        }
    }

    override fun getLayoutId() = R.layout.activity_training

    override fun getToolbarTitle(): String? {
        return gesture.title
    }

    override fun onViewCreated() {
        super.onViewCreated()

        // Show description
        descriptionTextView.text = gesture.description

        // Initialize GestureView
        gestureView = gesture.view(this)
        gestureView.callback = callback
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
        if (intent.getBooleanExtra("launch", false)) {
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
}