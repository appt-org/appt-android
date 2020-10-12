package nl.appt.ui.training

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_training.view.*
import nl.appt.R
import nl.appt.accessibility.view.accessibility
import nl.appt.extensions.isAccessibilityServiceEnabled
import nl.appt.extensions.isApptServiceEnabled
import nl.appt.services.ApptService

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingFragment : Fragment() {

    private val TAG = "TrainingFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training, container, false)

        view.text.visibility = View.GONE
        view.text.accessibility.label = "Traininen beschrijving"
        view.text.accessibility.action = "Actie beschrijving"

        if (!isApptServiceEnabled()) {
            enableApptService()
        }

        view.gesture.setOnClickListener { view ->
            Log.d(TAG, "Clicked")
        }

//        view.setOnHoverListener { view, motionEvent ->
//            Log.d(TAG, "Hover")
//
//            if (Accessibility.isTalkBackEnabled(context)) {
//                view.onTouchEvent(motionEvent)
//                true
//            } else {
//                false
//            }
//        }
//
//        view.setOnClickListener { view ->
//            Log.d(TAG, "Clicked")
//        }
//
//        view.setOnTouchListener { view, motionEvent ->
//            Log.d(TAG, "Touched")
//            false
//        }

        return view
    }

    private fun isApptServiceEnabled(): Boolean {
        val name = ApptService::class.java.name

        (context?.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).let { manager ->
            val services = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            for (service in services) {
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
}