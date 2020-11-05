package nl.appt.widgets

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.view_toolbar.view.*
import nl.appt.R

/**
 * Created by Jan Jaap de Groot on 19/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class ToolbarActivity : BaseActivity() {

    var toolbar: Toolbar? = null

    abstract fun getToolbarTitle(): String?

    override fun onViewCreated() {
        super.onViewCreated()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = getToolbarTitle()
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

    override fun setTitle(titleId: Int) {
        super.setTitle("")

        toolbar?.toolbarTitle?.let {
            it.text = getString(titleId)
        }
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("")

        toolbar?.toolbarTitle?.let {
            it.text = title
        }
    }
}