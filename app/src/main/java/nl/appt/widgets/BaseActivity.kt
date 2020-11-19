package nl.appt.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.announce
import nl.appt.extensions.setVisible
import nl.appt.extensions.toast

/**
 * Created by Jan Jaap de Groot on 19/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayoutId(): Int

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        onViewCreated()
    }

    open fun onViewCreated() {
        // Can be overridden
    }

    fun setLoading(loading: Boolean) {
        if (loading) {
            Accessibility.announce(this, "Aan het laden")
        }

        findViewById<ProgressBar>(R.id.progressBar)?.let { progressBar ->
            progressBar.setVisible(loading)
        }
    }

    inline fun <reified T : Activity> startActivity(requestCode: Int = -1, options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
        val intent = Intent(this, T::class.java)
        intent.init()
        startActivityForResult(intent, requestCode, options)
    }
}