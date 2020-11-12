package nl.appt.widgets

import kotlinx.android.synthetic.main.activity_text.*
import nl.appt.R
import nl.appt.extensions.getText
import nl.appt.extensions.getTitle

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TextActivity : ToolbarActivity() {

    override fun getLayoutId() = R.layout.activity_text

    override fun getToolbarTitle() = intent.getTitle()

    override fun onViewCreated() {
        super.onViewCreated()

        textView.text = intent.getText()
    }
}
