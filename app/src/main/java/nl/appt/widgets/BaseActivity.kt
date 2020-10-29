package nl.appt.widgets

import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import nl.appt.R
import nl.appt.extensions.setVisible

/**
 * Created by Jan Jaap de Groot on 19/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun getViewId(): Int

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
        setContentView(getViewId())
        onViewCreated()
    }

    open fun onViewCreated() {
        // Can be overridden
    }

    fun setLoading(loading: Boolean) {
        findViewById<ProgressBar>(R.id.progressBar)?.let { progressBar ->
            progressBar.setVisible(loading)
        }
    }
}