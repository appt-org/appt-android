package nl.appt.ui.training

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_training.*
import nl.appt.R
import nl.appt.accessibility.activity.accessibility
import nl.appt.accessibility.view.accessibility
import nl.appt.model.Gesture
import nl.appt.services.ApptService

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingActivity: AppCompatActivity() {

    private val TAG = "TrainingActivity"

    private val gesture = Gesture.SWIPE_RIGHT

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getIntExtra("id", -1)?.let { gestureId ->
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

        gestureView.setOnClickListener { view ->
            Log.d(TAG, "Clicked")
        }

        gestureView.setOnHoverListener { view, motionEvent ->
            Log.d(TAG, "Hover")
            false
//            if (Accessibility.isTalkBackEnabled(this)) {
//                //view.onTouchEvent(motionEvent)
//                true
//            } else {
//                false
//            }
        }

        gestureView.setOnTouchListener { view, motionEvent ->
            Log.d(TAG, "Touched")
            false
        }

        val filter = IntentFilter()
        filter.addAction("GESTURE")
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        if (!isApptServiceEnabled()) {
            enableApptService()
        }
    }

    private fun isApptServiceEnabled(): Boolean {
        val name = ApptService::class.java.name

        (getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).let { manager ->
            val services = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            for (service in services) {
                Log.d(TAG, "Service: ${service.resolveInfo.serviceInfo.name}")

                if (service.resolveInfo.serviceInfo.name == name) {
                    return true
                }
            }
        }
        return false
    }

    private fun enableApptService() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "dispatchTouchEvent: $event")
        return super.dispatchTouchEvent(event)
    }
}