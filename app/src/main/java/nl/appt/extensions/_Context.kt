package nl.appt.extensions

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.github.kittinunf.fuel.core.FuelError
import nl.appt.R
import nl.appt.model.Article
import nl.appt.tabs.news.ArticleActivity
import java.util.*
import kotlin.concurrent.schedule

/** Networking **/

fun Context.hasInternet(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo?.isConnectedOrConnecting ?: false
}

/** Browser **/
private const val HOST = "appt.crio-server.com"
private const val KENNISBANK = "kennisbank"

fun Context.openWebsite(url: String) {
    val uri = Uri.parse(url)
    val segments = uri.pathSegments

    // Check if kennisbank article (appt.nl/kennisbank/)
    if (uri.host == HOST && segments.size > 0 && segments[0] == KENNISBANK) {
        // Load article using WebView in ArticleActivity
        val intent = Intent(this, ArticleActivity::class.java)
        intent.setArticleType(Article.Type.PAGE)
        intent.setUri(uri)
        startActivity(intent)
    } else {
        // Open URL in Chrome Tab
        openWebsite(uri)
    }
}

fun Context.openWebsite(uri: Uri) {
    val black = resources.getColor(R.color.black, null)
    val white = resources.getColor(R.color.white, null)

    val dark = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(black)
        .setNavigationBarColor(black)
        .setNavigationBarDividerColor(black)
        .setSecondaryToolbarColor(black)
        .build()

    val light = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(white)
        .setNavigationBarColor(white)
        .setNavigationBarDividerColor(white)
        .setSecondaryToolbarColor(white)
        .build()

    val intent = CustomTabsIntent.Builder()
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setUrlBarHidingEnabled(false)
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
        .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, dark)
        .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_LIGHT, light)
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

fun Context.showError(error: FuelError?, callback: (() -> Unit)? = null) {
    if (error != null) {
        when (error.response.statusCode) {
            404 -> showError(R.string.error_404, callback)
            500 -> showError(R.string.error_500, callback)
            503 -> showError(R.string.error_503, callback)
            else -> {
                if (!hasInternet()) {
                    showError(R.string.error_network, callback)
                } else if (error.causedByInterruption) {
                    showError(R.string.error_interrupted, callback)
                } else {
                    showError(error.localizedMessage, callback)
                }
            }
        }
    } else {
        showError(R.string.error_something, callback)
    }
}

/** Toast **/
fun toast(
    context: Context?,
    message: String,
    duration: Long = 3000,
    callback: (() -> Unit)? = null
) {
    if (context == null) {
        return
    }

    val builder = AlertDialog.Builder(context, R.style.Toast)
    builder.setCancelable(false)
    builder.setMessage(message)

    builder.setOnDismissListener {
        callback?.let {
            it()
        }
    }

    val dialog = builder.create()
    dialog.show()

    Timer().schedule(duration) {
        dialog.dismiss()
    }
}

fun toast(context: Context?, message: Int, duration: Long = 3000, callback: (() -> Unit)? = null) {
    toast(context, context?.getString(message) ?: "", duration, callback)
}

/** String **/

fun Context.getIdentifier(resourceType: String, resourceName: String): Int {
    return resources.getIdentifier(resourceName, resourceType, "nl.appt")
}

fun Context.getString(name: String): String {
    val identifier = getIdentifier("string", name)
    return if (identifier > 0) {
        getString(identifier)
    } else {
        name
    }
}