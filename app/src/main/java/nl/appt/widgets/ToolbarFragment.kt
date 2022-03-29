package nl.appt.widgets

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.view_toolbar.view.*
import nl.appt.R

/**
 * Created by Jan Jaap de Groot on 19/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class ToolbarFragment : BaseFragment() {

    var toolbar: Toolbar? = null

    abstract fun getTitle(): String?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)

        toolbar?.toolbarTitle?.let {
            it.text = getTitle()
        }

        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(toolbar)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackSelected()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onBackSelected() {
        // Can be overridden
    }
}