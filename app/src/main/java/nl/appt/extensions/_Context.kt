package nl.appt.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import com.github.kittinunf.fuel.core.FuelError
import nl.appt.R

/** Networking **/

fun Context.hasInternet(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo?.isConnectedOrConnecting ?: false
}

/** Browser **/

fun Context.openWebsite(url: String) {
    openWebsite(Uri.parse(url))
}

fun Context.openWebsite(uri: Uri) {
    val backgroundColor = resources.getColor(R.color.background, null)

    val builder = CustomTabsIntent.Builder()
    builder.addDefaultShareMenuItem()
    builder.setNavigationBarColor(backgroundColor)
    builder.setToolbarColor(backgroundColor)
    builder.setSecondaryToolbarColor(backgroundColor)

    val tabsIntent = builder.build()
    tabsIntent.intent.putExtra(CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING, false)

    tabsIntent.launchUrl(this, uri)
}

/** Dialog **/

fun Context.showDialog(title: String, message: String?, callback: (() -> Unit)? = null) {
    val builder = AlertDialog.Builder(this)

    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton(R.string.action_ok) { _, _ ->
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

fun Context.showError(error: FuelError?, callback: (() -> Unit)? = null) {
    error?.let {
        when (it.response.statusCode) {
            404 -> showError(R.string.error_404, callback)
            500 -> showError(R.string.error_500, callback)
            503 -> showError(R.string.error_503, callback)
            else -> {
                if (!hasInternet()) {
                    showError(R.string.error_network, callback)
                } else if (it.causedByInterruption) {
                    showError(R.string.error_interrupted, callback)
                } else {
                    showError(it.localizedMessage, callback)
                }
            }
        }
    } ?: run {
        showError(R.string.error_something, callback)
    }
}

/** Toast **/
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0) {
    val toast = Toast.makeText(this, message, duration)
    toast.setGravity(gravity, xOffset, yOffset)
    toast.show()
}