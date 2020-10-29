package nl.appt.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import nl.appt.R
import nl.appt.extensions.setVisible
import nl.appt.extensions.startActivity

/**
 * Created by Jan Jaap de Groot on 19/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class BaseFragment : Fragment() {

    abstract fun getViewId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getViewId(), container, false)
    }

    open fun willShow() {
        // Can be overridden
    }

    fun setLoading(loading: Boolean) {
        view?.findViewById<ProgressBar>(R.id.progressBar)?.let { progressBar ->
            progressBar.setVisible(loading)
        }
    }

    inline fun <reified T : Activity> startActivity(options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
        context?.startActivity<T>(options, init)
    }
}