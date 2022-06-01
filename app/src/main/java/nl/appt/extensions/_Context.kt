package nl.appt.extensions

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.github.kittinunf.fuel.core.FuelError
import nl.appt.R
import java.util.*
import kotlin.concurrent.schedule

/** Networking **/

fun Context.hasInternet(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo?.isConnectedOrConnecting ?: false
}

/** Browser **/

fun Context.openWebsite(url: String) {
    val uri = Uri.parse(url)
    openWebsite(uri)
}

fun Context.openWebsite(uri: Uri) {
    // Open non-http uri outside app
    uri.scheme?.let { scheme ->
        if (!scheme.startsWith("http")) {
            if (uri.scheme?.startsWith("mailto") == true) {
                // TODO: Improve mail handling
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            ContextCompat.startActivity(this, intent, null)
            return@openWebsite
        }
    }

    // Open http uri inside ChromeTab
    val darkColor = resources.getColor(R.color.dark, null)
    val lightColor = resources.getColor(R.color.light, null)

    val darkScheme = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(darkColor)
        .setNavigationBarColor(darkColor)
        .setNavigationBarDividerColor(darkColor)
        .setSecondaryToolbarColor(darkColor)
        .build()

    val lightScheme = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(lightColor)
        .setNavigationBarColor(lightColor)
        .setNavigationBarDividerColor(lightColor)
        .setSecondaryToolbarColor(lightColor)
        .build()

    val intent = CustomTabsIntent.Builder()
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setUrlBarHidingEnabled(false)
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
        .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, darkScheme)
        .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_LIGHT, lightScheme)
        .build()

    intent.launchUrl(this, uri)
}

/** Dialog **/

fun Context.showDialog(title: String, message: String?, callback: (() -> Unit)? = null) {
    val builder = AlertDialog.Builder(this, R.style.Dialog)

    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton(R.string.action_ok) { _, _ ->
        // Ignored, handled by on dismiss listener.
    }

    builder.setOnDismissListener {
        callback?.let {
            it()
        }
    }

    builder.create().show()
}

fun Context.showDialog(titleId: Int, messageId: Int?, callback: (() -> Unit)? = null) {
    val title = getString(titleId)
    val message = if (messageId != null) getString(messageId) else null
    showDialog(title, message, callback)
}

fun Context.showError(messageId: Int, callback: (() -> Unit)? = null) {
    showDialog(R.string.error, messageId, callback)
}

fun Context.showError(message: String?, callback: (() -> Unit)? = null) {
    if (message != null) {
        showDialog(getString(R.string.error), message, callback)
    } else {
        showDialog(R.string.error, R.string.error_something, callback)
    }
}

/** Toast **/
fun toast(
    context: Context?,
    message: String?,
    duration: Int = Toast.LENGTH_SHORT,
    callback: (() -> Unit)? = null
) {
    if (context == null || message == null) {
        return
    }

    Toast.makeText(context, message, duration).show()

    if (callback != null) {
        Timer().schedule(duration.toLong()) {
            callback()
        }
    }
}

fun toast(context: Context?, message: Int, duration: Int = Toast.LENGTH_SHORT, callback: (() -> Unit)? = null) {
    toast(context, context?.getString(message), duration, callback)
}

/** String **/

fun Context.getIdentifier(resourceType: String, resourceName: String): Int {
    return resources.getIdentifier(resourceName, resourceType, packageName)
}

fun Context.getString(name: String): String {
    val identifier = getIdentifier("string", name)
    return if (identifier > 0) {
        getString(identifier)
    } else {
        name
    }
}