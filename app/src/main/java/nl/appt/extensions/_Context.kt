package nl.appt.extensions

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.view.accessibility.AccessibilityManager
import nl.appt.services.ApptService

/**
 * Checks if the given class is enabled as accessibility service.
 */
fun <T> Context.isAccessibilityServiceEnabled(clazz: Class<T>): Boolean {
    (getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).let { manager ->
        val services = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (service in services) {
            if (service.resolveInfo.serviceInfo.name == clazz.name) {
                return true
            }
        }
    }
    return false
}

fun Context.isApptServiceEnabled(): Boolean {
    return isAccessibilityServiceEnabled(ApptService::class.java)
}