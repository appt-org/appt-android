package nl.appt.services

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import nl.appt.R
import nl.appt.extensions.startActivity
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Constants
import nl.appt.tabs.training.TrainingActivity
import java.io.Serializable

/**
 * This is where the magic happens.
 * Running an AccessibilityService makes it "bypass" TalkBack.
 * The ApptService hooks into accessibility events and gestures.
 *
 * Created by Jan Jaap de Groot on 09/10/2020
 * Copyright 2020 Stichting Appt
 */
class ApptService: AccessibilityService() {

    private val TAG = "ApptService"
    private val TRAINING_CLASS_NAME = TrainingActivity::class.java.name

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")

        // Start TrainingActivity
        startTraining()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "Service connected")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onInterrupt() {
        Log.i(TAG, "onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                Log.d(TAG, "Changed window to: ${event.packageName}")

                // Kill service if window has changed or if TrainingActivity is no longer active.
                if (event.packageName != this.packageName || !isTraining()) {
                    kill()
                }
            }
        }
    }

    override fun onGesture(gestureId: Int): Boolean {
        Log.i(TAG, "onGesture: $gestureId")

        // Kill service if no longer training
        if (!isTraining()) {
            kill()
            return false
        }

        // Broadcast gesture to TrainingActivity
        AccessibilityGesture.from(gestureId)?.let { gesture ->
            broadcast(Constants.SERVICE_GESTURE, gesture)
        }

        return true
    }

    private fun broadcast(key: String, value: Serializable) {
        val intent = Intent(Constants.SERVICE_ACTION)
        intent.setPackage(packageName)
        intent.putExtra(key, value)
        applicationContext.sendBroadcast(intent)
    }

    private fun kill() {
        Toast.makeText(this, R.string.service_killed, Toast.LENGTH_SHORT).show()

        broadcast(Constants.SERVICE_KILLED, true)

        disableSelf()
    }

    private fun isTraining(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getRunningTasks(1).firstOrNull()?.topActivity?.let { activity ->
            return activity.className == TRAINING_CLASS_NAME
        }
        return false
    }

    private fun startTraining() {
        startActivity<TrainingActivity> {
            putExtra("launch", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}