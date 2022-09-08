package nl.appt.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import nl.appt.extensions.toast
import nl.appt.helpers.Events

/**
 * Created by Jan Jaap de Groot on 19/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class BaseActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    lateinit var events: Events

    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        events = Events(firebaseAnalytics)

        setContentView(getLayoutId())
        onViewCreated()
    }

    open fun onViewCreated() {
        // Can be overridden
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun setTitle(titleId: Int) {
        super.setTitle("")
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("")
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun toast(message: String, duration: Int = Toast.LENGTH_SHORT, callback: (() -> Unit)? = null) {
        toast(this, message, duration, callback)
    }

    fun toast(message: Int, duration: Int = Toast.LENGTH_SHORT, callback: (() -> Unit)? = null) {
        toast(this, message, duration, callback)
    }

    inline fun <reified T : Activity> startActivity(requestCode: Int = -1, options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
        val intent = Intent(this, T::class.java)
        intent.init()
        startActivityForResult(intent, requestCode, options)
    }
}