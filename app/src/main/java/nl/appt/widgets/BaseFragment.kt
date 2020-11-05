package nl.appt.widgets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.announce
import nl.appt.extensions.setVisible

/**
 * Created by Jan Jaap de Groot on 19/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class BaseFragment : Fragment() {

    var isLoading: Boolean = false
        set(value) {
            field = value

            if (value) {
                Accessibility.announce(context, getString(R.string.loading))
            }

            view?.findViewById<ProgressBar>(R.id.progressBar)?.let { progressBar ->
                progressBar.setVisible(value)
            }
        }

    abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    open fun willShow() {
        // Can be overridden
    }

    inline fun <reified T : Activity> startActivity(requestCode: Int = -1, options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
        context?.let { context ->
             val intent = Intent(context, T::class.java)
            intent.init()
            startActivityForResult(intent, requestCode, options)
        }
    }
}