package nl.appt.tabs.training

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_training.*
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.activity.accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.accessibility.view.accessibility
import nl.appt.extensions.startActivity
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Constants
import nl.appt.model.Gesture
import nl.appt.services.ApptService
import nl.appt.views.GestureView
import nl.appt.views.GestureViewCallback

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingActivity: AppCompatActivity() {

    private val TAG = "TrainingActivity"
    private val APPT_SERVICE = ApptService::class.java.name

    private val gesture = Gesture.SWIPE_UP_AND_DOWN
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
            (intent?.getSerializableExtra(Constants.SERVICE_GESTURE) as AccessibilityGesture).let { gesture ->
                gestureView.onAccessibilityGesture(gesture)
            }
        }
    }

    private val callback = object : GestureViewCallback {
        override fun correct(gesture: Gesture) {
            Toast.makeText(baseContext, "Gebaar correct uitgevoerd!", Toast.LENGTH_SHORT).show()
        }

        override fun incorrect(gesture: Gesture, feedback: String?) {
            Log.d(TAG, feedback)
            Toast.makeText(baseContext, feedback, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        title = gesture.title

        // Initialize GestureView
        gestureView = gesture.view(this)
        gestureView.setBackgroundColor(R.color.primary)
        gestureView.callback = callback
        gestureView.accessibility.label = gesture.description
        container.addView(gestureView)
        accessibility.elements = arrayOf(gestureView)

        // Listen to events from ApptService
        val filter = IntentFilter()
        filter.addAction(Constants.SERVICE_ACTION)
        registerReceiver(receiver, filter)
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

        if (Accessibility.isTalkBackEnabled(this) && !isApptServiceEnabled()) {
            enableApptService()
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
                intent.flags = Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                onBackPressed()
            }
            .show()
    }
}