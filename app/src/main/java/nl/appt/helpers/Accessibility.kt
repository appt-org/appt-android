package nl.appt.helpers

import android.content.Context
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityEventCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

object Accessibility {

    fun setClassName(view: View, className: String) {
        ViewCompat.setAccessibilityDelegate(view, object : AccessibilityDelegateCompat() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View,
                info: AccessibilityNodeInfoCompat
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info.className = className
            }
        })
    }

    fun setButton(view: View, button: Boolean = true) {
        val className = if (button) Button::class.java.name else ""
        setClassName(view, className)
    }

    fun announce(context: Context, message: String) {
        val event = AccessibilityEvent.obtain(AccessibilityEventCompat.TYPE_ANNOUNCEMENT)
        event.text.add(message)
        event.className = Context::class.java.name
        event.packageName = context.packageName

        ContextCompat.getSystemService(context, AccessibilityManager::class.java)?.let { accessibilityManager ->
            if (accessibilityManager.isEnabled) {
                accessibilityManager.sendAccessibilityEvent(event)
            }
        }
    }
}