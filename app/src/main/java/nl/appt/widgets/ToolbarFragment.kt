package nl.appt.widgets

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

        toolbar?.findViewById<TextView>(R.id.toolbarTitle)?.let {
            it.text = getTitle()
        }

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
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