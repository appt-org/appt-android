package nl.appt.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

inline fun <reified T : Activity> Context.startActivity(options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Activity> Activity.launchActivity(options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
    startActivity<T>(options, init)
}