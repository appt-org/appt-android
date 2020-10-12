package nl.appt

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import nl.appt.extensions.isApptServiceEnabled

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_training, R.id.navigation_knowledge, R.id.navigation_information))
        val navController = findNavController(R.id.navigationController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        val contentView: View = findViewById(android.R.id.content)

        contentView.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onRequestSendAccessibilityEvent(host: ViewGroup?, child: View?, event: AccessibilityEvent): Boolean {
                Log.d(TAG, "onRequestSendAccessibilityEvent: $event")
                return super.onRequestSendAccessibilityEvent(host, child, event)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val enabled = isApptServiceEnabled()
        Log.i(TAG, "Service enabled: $enabled")
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "dispatchTouchEvent: $event")
        return super.dispatchTouchEvent(event)
    }
}