package nl.appt.tabs.training

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
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
import nl.appt.model.Constants
import nl.appt.model.Gesture
import nl.appt.services.ApptService


/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingActivity: AppCompatActivity() {

    private val TAG = "TrainingActivity"
    private val APPT_SERVICE = ApptService::class.java.name
    private val gesture = Gesture.SWIPE_RIGHT

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getIntExtra(Constants.SERVICE_GESTURE, -1)?.let { gestureId ->
                if (gestureId == gesture.gestureId) {
                    Log.d(TAG, "Correct gesture!")
                } else {
                    Log.d(TAG, "Incorrect gesture: $gestureId")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        title = gesture.title
        gestureView.accessibility.label = gesture.description

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
                startActivity(intent)
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                onBackPressed()
            }
            .show()
    }
}