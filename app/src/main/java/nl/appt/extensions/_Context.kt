package nl.appt.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import com.github.kittinunf.fuel.core.FuelError
import nl.appt.R


/** Activity **/

inline fun <reified T : Activity> Context.startActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Activity> Activity.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    startActivity<T>(options, init)
}

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
    AppCompatResources.getDrawable(this, R.drawable.icon_back)?.let { icon ->
        builder.setCloseButtonIcon(icon.toBitmap())
    }

    val intent = builder.build()
    intent.launchUrl(this, uri)
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